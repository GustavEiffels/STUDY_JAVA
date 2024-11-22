## UPLOAD METHOD 를 refactoring 하려고 한다.
```java

    public String upload(SftpCredentials credentials, String path, MultipartFile file) {

        // ** file 이 존재하는지 확인
        if(file == null)
            throw new CustomSftpException(SFTP_STATUS.NOT_FOUND_FILE);

        // ** file 확장자 추출
        String extension = getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));

        // ** file type 및 확장자 확인
        if(!isValidType(Objects.requireNonNull(file.getContentType()),extension))
            throw new CustomSftpException(SFTP_STATUS.ONLY_IMAGE);

        ChannelSftp channel = createChannel(credentials);

        // ** Sanitized Path
        String sanitizedPath = generateSanitizedPath(path);


        String newFilePath = generateNewFilePath(sanitizedPath,file.getOriginalFilename(),extension);
        // ** upload
        try(InputStream inputStream = file.getInputStream()){
            channel.put(inputStream,newFilePath);
        }
        catch (IOException ioE){
            throw new CustomSftpException(SFTP_STATUS.IOE,ioE.getLocalizedMessage());
        }
        catch (SftpException sftpE){
            throw new CustomSftpException(SFTP_STATUS.SFTP_ERROR,sftpE.getLocalizedMessage());
        }
        finally {
            disconnect();
        }
        return newFilePath;
    }
    private String getFileExtension(String fileOriginalName){
        String extension = fileOriginalName.substring(
                fileOriginalName.lastIndexOf(".") + 1).toLowerCase();

        if(!extension.equals("jpg")&&!extension.equals("jpeg")&&!extension.equals("png")){
            throw new CustomSftpException(SFTP_STATUS.NOT_ALLOW_FILE_TYPE);
        }
        return extension;
    }
    private String generateSanitizedPath(String path) {
        if (path == null || path.trim().isEmpty()) {
            return "/";
        }
        if(path.equals("/")){
            return path;
        }
        return path.replaceAll("/{2,}", "/").replaceAll("/$", "");
    }
    private String generateNewFilePath(String path, String originalFilename,String extension) {
        // ** convert file name
        if( originalFilename == null || !originalFilename.contains(".") ){
            throw new CustomSftpException(SFTP_STATUS.NOT_ALLOW_FILE_TYPE);
        }

        // ** extract Extension of the file
        // +1 => Available Find Hidden File
        if(!hasText(extension)) throw new CustomSftpException(SFTP_STATUS.NOT_ALLOW_FILE_TYPE);
        String newFileName = UUID.randomUUID()+"."+extension;

        return path+"/"+newFileName;
    }
    private boolean isValidType(String contentType,String extensionName) {

        return (contentType.equals("image/jpeg") && extensionName.equals("jpg")) ||
                (contentType.equals("image/png") && extensionName.equals("png"));
    }
    private boolean isValidPath(ChannelSftp channel, String path){
        try{

            channel.ls(path);
            return true;
        }
        catch (SftpException e){
            System.out.println("SFTP Exception : "+e.getLocalizedMessage());
            return false;
        }
    }

    // description: disconnect sftp session, channel
    private void disconnect(){
        Session session     = sessionHolder.get();
        ChannelSftp channel = channelHolder.get();

        if(channel!=null&&channel.isConnected()){
            channel.disconnect();
            channelHolder.remove();
        }

        if(session!=null&&session.isConnected()){
            session.disconnect();
            sessionHolder.remove();
        }
    }
```


> 아무리 TDD 를 공부한다는 이유이지만 너무 난개발을 한것 같아 수정하려고 한다.

```java
    /**
     *  1. 입력 확인
     *  2. 파일 확인
     *  3. 새로운 경로 생성
     *  4. upload
     *  5. 새로운 경로 반환
     *
     * @param credentials
     * @param path
     * @param file
     * @return
     */
```


## REFACTORING
```java

    /**
     *  1. 입력 확인
     *  2. 파일 확인
     *  3. 새로운 경로 생성
     *  4. upload
     *  5. 새로운 경로 반환
     *
     * @param credentials
     * @param path
     * @param file
     * @return
     */
    public String upload(SftpCredentials credentials, String path, MultipartFile file) {

        // ** 파일 정보 확인
        SftpFileInfo fileInfo   = extractFileInfo(file);

        // ** SFTP 채널 생성
        ChannelSftp channel     = createChannel( (credentials!=null) ? credentials : new SftpConnectionProperties().connection());

        // ** 새로운 파일 경로 생성
        String newFilePath      = generateFilePath(channel,path,fileInfo.getExtension());

        // ** upload
        try(InputStream inputStream = file.getInputStream()){
            channel.put(inputStream,newFilePath);
        }
        catch (IOException ioE){
            throw new CustomSftpException(SFTP_STATUS.IOE,ioE.getLocalizedMessage());
        }
        catch (SftpException sftpE){
            throw new CustomSftpException(SFTP_STATUS.SFTP_ERROR,sftpE.getLocalizedMessage());
        }
        finally {
            disconnect();
        }
        return newFilePath;
    }

        /**
         *
         * @param multipartFile
         * @return
         */
        public SftpFileInfo extractFileInfo(MultipartFile multipartFile) {
            assert  multipartFile != null;
            String fileOriginalNm = multipartFile.getOriginalFilename();    // FILE ORIGINAL
            String fileType       = multipartFile.getContentType();         // FILE TYPE
            assert fileOriginalNm != null;
            String fileExtension  = fileOriginalNm
                    .substring(fileOriginalNm.lastIndexOf(".") + 1)
                    .toLowerCase();

            boolean isAlloyFileType = false;

            switch (fileType){
                case "image/jpeg" ->{
                    if( fileExtension.equals("jpg") || fileExtension.equals("jpeg") )
                        isAlloyFileType = true;
                }
                case "image/png" ->{
                    if( fileExtension.equals("png"))
                        isAlloyFileType = true;
                }
            }
            if(!isAlloyFileType)
                throw new CustomSftpException(SFTP_STATUS.NOT_ALLOW_FILE_TYPE);

            return new SftpFileInfo(fileExtension,fileType);
        }


        /**
         *
         * @param channel
         * @param path
         * @param extension
         * @return
         */
        private String generateFilePath(ChannelSftp channel,String path,String extension){
            // ** Path 수정
            path.trim()
                    .replaceAll("/{2,}", "/")
                    .replaceAll("/$", "");
            try {
                channel.ls(path);
                return path+"/"+UUID.randomUUID()+"."+extension;
            }
            catch (SftpException e){
                throw new CustomSftpException(SFTP_STATUS.NOT_AVAILABLE_PATH);
            }
        }

        /**
         *
         * @param channel
         * @param path
         * @return
         */
        private boolean isValidPath(ChannelSftp channel, String path){
            try{
                channel.ls(path);
                return true;
            }
            catch (SftpException e){
                System.out.println("SFTP Exception : "+e.getLocalizedMessage());
                return false;
            }
        }
```
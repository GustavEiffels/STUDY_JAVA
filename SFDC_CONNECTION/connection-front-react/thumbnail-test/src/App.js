import { useState } from 'react';
import './App.css';

function App() {

  const [loadImage     , setLoadImage]          = useState(null)
  const [fileStateLabel, setFileStateLabel]     = useState('파일 올리기')
  const [selectedFile  , setSelectedFile]       = useState(null)
  const [thumbnail     , setThumbnail]          = useState(null)
  const [path          , setPath]               = useState(null)
  const [inputValue    , setInputValue]         = useState(null)


  const onLoadImage = async () => {
    try {
      console.log('inputValue : ',inputValue)
      const response = await fetch("http://localhost:8080/download", {
        method: "POST",
        headers: {
          "Content-Type": "application/json", // JSON 데이터를 보내기 위한 Content-Type 설정
        },
        body: JSON.stringify({ path: inputValue }),
      });
  
      if (!response.ok) {
        throw new Error("Failed to download image");
      }
  
      const blob     = await response.blob(); // Convert the response to a Blob
      const imageUrl = URL.createObjectURL(blob); // Create an object URL
      setLoadImage(imageUrl); // Set the URL to the state
  
      // Generate a thumbnail
      const img = new Image();
      img.src = imageUrl;
  
      img.onload = () => {
        const canvas = document.createElement("canvas");
        const ctx    = canvas.getContext("2d");
  
        const MAX_WIDTH = 100;  // Thumbnail width
        const MAX_HEIGHT = 100; // Thumbnail height
  
        let width = img.width;
        let height = img.height;
  
        // Adjust dimensions to maintain aspect ratio
        if (width > height) {
          if (width > MAX_WIDTH) {
            height *= MAX_WIDTH / width;
            width = MAX_WIDTH;
          }
        } else {
          if (height > MAX_HEIGHT) {
            width *= MAX_HEIGHT / height;
            height = MAX_HEIGHT;
          }
        }
  
        canvas.width = width;
        canvas.height = height;
  
        // Draw the image on the canvas with new dimensions
        ctx.drawImage(img, 0, 0, width, height);
  
        // Convert canvas to a base64 URL and set it as the thumbnail
        const thumbnailUrl = canvas.toDataURL("image/jpeg");
        setThumbnail(thumbnailUrl);
      };
    } catch (error) {
      console.error("Error loading image:", error);
    }
  };


  const uploadClick = async()=>{
    const formData = new FormData();
    formData.append("file", selectedFile);

    const response = await fetch("http://localhost:8080/upload", {
      method: "POST",
      body: formData, // FormData를 요청 본문으로 설정
      credentials: "include", // allowCredentials 지원
    })
    
    response.json().then(res=>{
      console.log(res)
      console.log('res.path : ',res.path)
      setPath(res.path)
      setInputValue(res.path)
    })
  }


  const onChangeFileSelected = (e)=>{
    const selectedFile = e.target.files[0];
    setSelectedFile(selectedFile)
    if(selectedFile != null) {
      setFileStateLabel('업로드 완료')
    }
  }


  return (
    <div className="outlayer">
      <div className='wrapper'>
        <div className='uploadArea'>
          <label for='fileInput'>{fileStateLabel}</label>
          <input id='fileInput' type='file' onChange={onChangeFileSelected}></input>
          <button onClick={uploadClick}> Upload </button>
        </div>
        <div className='imageLoadList'>
          <div className='item'>
            <label>일반 사진 로드</label>
            <img src={loadImage} alt="Loaded" /> 
          </div>
          <div className='item'>
            <label>썸네일</label>
            <img src={thumbnail} alt="Loaded" /> 

          </div>
        </div>
        <div className='inputter'>
          <label>Load Image Path</label>
          <input value={inputValue}></input>

        </div>
        <button onClick={onLoadImage}>Load Image Button</button>
      </div>
    </div>
  );
}

export default App;

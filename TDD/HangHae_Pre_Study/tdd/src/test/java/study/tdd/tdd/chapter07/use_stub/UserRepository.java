package study.tdd.tdd.chapter07.use_stub;

public interface UserRepository {
    void save(User user);

    User findById(String id);
}

package kr.teammanagers.tag.application.lambda;

@FunctionalInterface
public interface TagRemovalAction<T> {
    void execute(T entity);
}

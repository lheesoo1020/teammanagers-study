package kr.teammanagers.tag.application.lambda;

@FunctionalInterface
public interface TagAction {
    void execute(String tagName);
}

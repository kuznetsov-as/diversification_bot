package commands.replyCommand;

import enums.UserState;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(ReplyCommandAnnotationContainer.class)
public @interface ReplyCommandAnnotation {
    UserState name();
    String description() default "";
}


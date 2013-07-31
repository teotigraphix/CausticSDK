package com.teotigraphix.caustk.pattern;

import com.teotigraphix.caustk.controller.IControllerComponent;

public interface IPatternManager extends IControllerComponent {

    Pattern playPattern(int last);

    Pattern getPattern();

    void playNextPattern();

    void setNextPattern(int last);

    Pattern getPendingPattern();

    void configure(Pattern pattern);

    void commit(Pattern pendingPattern);

}

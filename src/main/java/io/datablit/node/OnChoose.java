package io.datablit.node;

import io.datablit.meta.Choice;
import io.datablit.meta.Meta;

/**
 *
 */
public interface OnChoose {
    Meta choose(Selection sel, Choice choice);
}

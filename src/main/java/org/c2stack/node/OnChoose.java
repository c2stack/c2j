package org.c2stack.node;

import org.c2stack.meta.Choice;
import org.c2stack.meta.Meta;

/**
 *
 */
public interface OnChoose {
    Meta choose(Selection sel, Choice choice);
}

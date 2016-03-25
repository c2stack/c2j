package org.conf2.data;

import org.conf2.schema.Choice;
import org.conf2.schema.Meta;

/**
 *
 */
public interface OnChoose {
    Meta choose(Selection sel, Choice choice);
}

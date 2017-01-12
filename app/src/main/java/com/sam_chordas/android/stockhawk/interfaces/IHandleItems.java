package com.sam_chordas.android.stockhawk.interfaces;

import java.util.List;

public interface IHandleItems<T> {
    void handleItems(List<T> items);
}
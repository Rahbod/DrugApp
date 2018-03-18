package com.example.behnam.app.fastscroll;

/**
 * Created by Behnam on 3/7/2018.
 */

public class AlphabetItem {
    public int position;
    public String word;
    public boolean isActive;

    public AlphabetItem(int position, String word, boolean isActive) {
        this.position = position;
        this.word = word;
        this.isActive = isActive;
    }
}

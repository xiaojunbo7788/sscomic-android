package com.ssreader.novel.ui.localshell.localapp;

import com.ssreader.novel.model.Book;
import com.ssreader.novel.model.Downoption;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

@Entity
public class LocalBook {

    @Id
    public long book_id;

    @Index
    public String Local_path;

    public String name;

    public int isLike; // 0 --> 不喜欢

    public LocalBook() {
    }

    public LocalBook(String name,String local_path) {
        Local_path = local_path;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Book) {
            return (Local_path.equals(((LocalBook) obj).Local_path));
        }
        return super.equals(obj);

    }

    @Override
    public int hashCode() {
        return Local_path.hashCode();
    }
}

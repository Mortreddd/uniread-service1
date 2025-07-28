package com.bsit.uniread.application.services.chapter;

import com.bsit.uniread.domain.entities.Follow;
import com.bsit.uniread.domain.entities.chapter.Chapter;

import java.util.List;

public interface Broadcaster {
    public void publish(Chapter chapter, List<Follow> followers);
}

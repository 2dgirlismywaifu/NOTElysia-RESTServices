/*
 * Copyright By @2dgirlismywaifu (2023) .
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.notelysia.newsandroidservices.jparepo;

import com.notelysia.newsandroidservices.model.SyncNewsFavourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SyncNewsFavouriteRepo extends JpaRepository<SyncNewsFavourite, Integer> {
    //Delete news favourite by user_id, url, title, image_url, source_name
    @Transactional
    @Modifying
    @Query("DELETE FROM SyncNewsFavourite s WHERE s.user_id = ?1 AND s.url = ?2 AND s.title = ?3 " +
            "AND s.image_url = ?4 AND s.source_name = ?5")
    void deleteNewsFavourite(String userId, String url, String title,
                                                              String imageUrl, String sourceName);

    //Get news favourite by user_id
    @Query("SELECT s FROM SyncNewsFavourite s WHERE s.user_id = ?1")
    List<SyncNewsFavourite> findByUserId(int userId);

    //Get news favourite exist by user_id, url, title, image_url, source_name
    @Query("SELECT s FROM SyncNewsFavourite s WHERE s.user_id = ?1 AND s.url = ?2 AND s.title = ?3 " +
            "AND s.image_url = ?4 AND s.source_name = ?5")
    SyncNewsFavourite findSyncNewsFavouriteBy(int userId, String sourceId,
                                                    String title, String imageUrl,
                                                    String sourceName);

}

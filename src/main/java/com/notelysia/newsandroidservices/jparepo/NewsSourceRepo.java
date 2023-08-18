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

import com.notelysia.newsandroidservices.model.NewsSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NewsSourceRepo extends JpaRepository<NewsSource, Long> {
    //Query to get all news source
    @Query(
            "FROM NewsSource newsSource, ImageInformation imageInfo " +
            "WHERE newsSource.source_id = imageInfo.source_id")
    List<NewsSource> findAllNewsSource();
    //Query to get all news source for the user login with email and password
    @Query(
            "FROM NewsSource newsSource, ImageInformation imageInfo, SyncSubscribe sync " +
            "WHERE newsSource.source_id = imageInfo.source_id AND newsSource.source_id = sync.source_id AND sync.user_id = ?1")
    Optional<NewsSource> findByUserEmailId(int user_id);
    //Query to get all news source for the user login with SSO
    @Query(
            "FROM NewsSource newsSource, ImageInformation imageInfo, SyncSubscribeSSO sync " +
            "WHERE newsSource.source_id = imageInfo.source_id AND newsSource.source_id = sync.source_id AND sync.user_id = ?1")
    Optional<NewsSource> findByUserSSOId(int user_id);
}
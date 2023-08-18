/*
 * Copyright By @2dgirlismywaifu (2023) .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.notelysia.newsandroidservices.controller;

import com.notelysia.newsandroidservices.config.DecodeString;
import com.notelysia.newsandroidservices.config.RandomNumber;
import com.notelysia.newsandroidservices.jparepo.SyncNewsFavouriteRepo;
import com.notelysia.newsandroidservices.jparepo.SyncSubscribeRepo;
import com.notelysia.newsandroidservices.model.SyncNewsFavourite;
import com.notelysia.newsandroidservices.model.SyncSubscribe;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v2")
@Tag(name = "Standard Account Favourite Controller", description = "API for Standard Account Favourite Controller")
public class StandardAccountFavouriteController {
    DecodeString decodeString = new DecodeString();
    SyncSubscribe syncSubscribe;
    SyncNewsFavourite syncNewsFavourite;
    @Autowired
    SyncSubscribeRepo syncSubscribeRepo;
    @Autowired
    SyncNewsFavouriteRepo syncNewsFavouriteRepo;
    private String getDecode (byte[] data) {
        return decodeString.decodeString(data);
    }

    //Insert source subscribe from user
    @PostMapping(value = "/account/favourite/add", params = {"userid", "sourceid"})
    public ResponseEntity<HashMap<String, String>> userSourceSubscribe (
            @Parameter(name = "userid", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "userid") String user_id,
            @Parameter(name = "sourceid", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "sourceid") String source_id) {
        String sync_id_random = new RandomNumber().generateSSONumber();
        syncSubscribe = new SyncSubscribe(Integer.parseInt(getDecode(sync_id_random.getBytes())),
                Integer.parseInt(getDecode(user_id.getBytes())), Integer.parseInt(getDecode(source_id.getBytes())));
        syncSubscribeRepo.save(syncSubscribe);
        return new ResponseEntity<>(new HashMap<>(){
            {
                put("sync_id", String.valueOf(syncSubscribe.getSync_id()));
                put("user_id", String.valueOf(syncSubscribe.getUser_id()));
                put("source_id", String.valueOf(syncSubscribe.getSource_id()));
                put("status", "success");
            }
        }, org.springframework.http.HttpStatus.OK);
    }

    //insert news favourite
    //services will use params: user_id, url, title, image_url, source_name
    @PostMapping(value = "/account/favourite/news/add", params = {"userid", "url", "title", "imageurl", "pubdate", "sourcename"})
    public ResponseEntity<HashMap<String, String>> userNewsFavourite (
            @Parameter(name = "userid", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "userid") String user_id,
            @Parameter(name = "url", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "url") String url,
            @Parameter(name = "title", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "title") String title,
            @Parameter(name = "imageurl", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "imageurl") String image_url,
            @Parameter(name = "pubdate", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "pubdate") String pub_date,
            @Parameter(name = "sourcename", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "sourcename") String source_name) {

        String favourite_id_random = new RandomNumber().generateSSONumber();
        syncNewsFavourite = new SyncNewsFavourite(Integer.parseInt(getDecode(favourite_id_random.getBytes())),
                Integer.parseInt(getDecode(user_id.getBytes())), getDecode(url.getBytes()), getDecode(title.getBytes()),
                getDecode(image_url.getBytes()), getDecode(pub_date.getBytes()), getDecode(source_name.getBytes()));
        syncNewsFavouriteRepo.save(syncNewsFavourite);
        return new ResponseEntity<>(new HashMap<>(){
            {
                put("favourite_id", String.valueOf(syncNewsFavourite.getFavourite_id()));
                put("user_id", String.valueOf(syncNewsFavourite.getUser_id()));
                put("url", syncNewsFavourite.getUrl());
                put("title", syncNewsFavourite.getTitle());
                put("image_url", syncNewsFavourite.getImage_url());
                put("pubdate", syncNewsFavourite.getPubdate());
                put("source_name", syncNewsFavourite.getSource_name());
                put("status", "success");
            }
        }, org.springframework.http.HttpStatus.OK);
    }

    //Unsubscribe source from user (delete from tabale)
    //services will use params: user_id, source_id
    @DeleteMapping(value = "/account/favourite/delete", params = {"userid", "sourceid"})
    public ResponseEntity<HashMap<String, String>> userSourceUnsubscribe (
            @Parameter(name = "userid", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "userid") String user_id,
            @Parameter(name = "sourceid", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "sourceid") String source_id) {
        syncSubscribeRepo.deleteByUserIdAndSourceId(Integer.parseInt(getDecode(user_id.getBytes())), getDecode(source_id.getBytes()));
        return new ResponseEntity<>(new HashMap<>(){
            {
                put("user_id", getDecode(user_id.getBytes()));
                put("source_id", getDecode(source_id.getBytes()));
                put("status", "deleted");
            }
        }, org.springframework.http.HttpStatus.OK);
    }

    //Delete news favourite from user (use params: user_id, url, title, image_url, source_name)
    @RequestMapping(value = "/account/favourite/news/delete", params = {"userid", "url", "title", "imageurl", "sourcename"}, method = RequestMethod.DELETE)
    public ResponseEntity<HashMap<String, String>> userNewsFavouriteDelete (
            @Parameter (name = "userid", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "userid") String user_id,
            @Parameter (name = "url", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "url") String url,
            @Parameter (name = "title", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "title") String title,
            @Parameter (name = "imageurl", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "imageurl") String image_url,
            @Parameter (name = "sourcename", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "sourcename") String source_name) {
        syncNewsFavouriteRepo.deleteNewsFavourite(getDecode(user_id.getBytes()), getDecode(url.getBytes()),
                getDecode(title.getBytes()), getDecode(image_url.getBytes()), getDecode(source_name.getBytes()));
        return new ResponseEntity<>(new HashMap<>(){
            {
                put("user_id", getDecode(user_id.getBytes()));
                put("url", getDecode(url.getBytes()));
                put("title", getDecode(title.getBytes()));
                put("image_url", getDecode(image_url.getBytes()));
                put("source_name", getDecode(source_name.getBytes()));
                put("status", "deleted");
            }
        }, org.springframework.http.HttpStatus.OK);
    }
    //show favourite news with params: user_id in sync_news_favourite
    @GetMapping(value = "/account/favourite/news/show", params = {"userid"})
    public ResponseEntity<HashMap<String, List<SyncNewsFavourite>>> userNewsFavouriteShow (
            @RequestParam(value = "userid") String user_id) {
        syncNewsFavouriteRepo.findByUserId(Integer.parseInt(getDecode(user_id.getBytes())));
        return new ResponseEntity<>(new HashMap<>() {
            {
                put("news_favourite", syncNewsFavouriteRepo.findByUserId(Integer.parseInt(getDecode(user_id.getBytes()))));
            }
        }, org.springframework.http.HttpStatus.OK);
    }

    //check news is favourite or not (use params: user_id, url, title, image_url, source_name)
    @GetMapping(value = "/account/favourite/news/check", params = {"userid", "url", "title", "imageurl", "sourcename"})
    public ResponseEntity<HashMap<String, String>> userSourceFavouriteCheck (
            @Parameter (name = "userid", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "userid") String user_id,
            @Parameter (name = "url", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "url") String url,
            @Parameter (name = "title", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "title") String title,
            @Parameter (name = "imageurl", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "imageurl") String image_url,
            @Parameter (name = "sourcename", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "sourcename") String source_name) {
        syncNewsFavourite = syncNewsFavouriteRepo.findSyncNewsFavouriteBy(Integer.parseInt(getDecode(user_id.getBytes())), getDecode(url.getBytes()),
                getDecode(title.getBytes()), getDecode(image_url.getBytes()), getDecode(source_name.getBytes()));
        return new ResponseEntity<>(new HashMap<>(){
            {
                put("user_id", String.valueOf(syncNewsFavourite.getUser_id()));
                put("url", syncNewsFavourite.getUrl());
                put("title", syncNewsFavourite.getTitle());
                put("image_url", syncNewsFavourite.getImage_url());
                put("source_name", syncNewsFavourite.getSource_name());
                put("status", "found");
            }
        }, org.springframework.http.HttpStatus.OK);
    }
    //Check source news in SYNC_SUBSCRIBE table. use params: user_id, source_id. DO NOT USE SYNC_NEWS_FAVOURITE TABLE
    @GetMapping(value = "/account/subscribe/check", params = {"userid","sourceid"})
    public ResponseEntity<HashMap<String, String>> userSourceSubscribeCheck (
            @RequestParam(value = "userid") String user_id,
            @RequestParam(value = "sourceid") String source_id) {
        syncSubscribe = syncSubscribeRepo.findByUserIdAndSourceId(Integer.parseInt(getDecode(user_id.getBytes())), getDecode(source_id.getBytes()));
        return new ResponseEntity<>(new HashMap<>(){
            {
                put("user_id", String.valueOf(syncSubscribe.getUser_id()));
                put("source_id", String.valueOf(syncSubscribe.getSource_id()));
                put("status", "found");
            }
        }, org.springframework.http.HttpStatus.OK);
    }
}
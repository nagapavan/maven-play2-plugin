/*
 * Copyright 2013 OW2 Nanoko Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.nanoko.playframework.mojo;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A small class to export issues from the current milestone.
 */
public class IssueExport {

    public static final String ISSUE_URL = "https://api.github.com/repos/nanoko-project/maven-play2-plugin/issues";

    @Test
    public void closedOnMilestone() throws IOException {
        // Replace milestone number
        // 1.2.3 => 1
        String json = IOUtils.toString(new URL(ISSUE_URL + "?milestone=1&state=closed"));
        JSONArray array = (JSONArray) JSONValue.parse(json);

        for (int i = array.size() -1; i >= 0; i--) {
            JSONObject issue = (JSONObject) array.get(i);
            System.out.println("* [#" + issue.get("number") + "](" + issue.get("url") + ")" + " - " + issue.get
                    ("title"));
        }



    }

    /**
     * {
     "url": "https://api.github.com/repos/nanoko-project/maven-play2-plugin/issues/13",
     "labels_url": "https://api.github.com/repos/nanoko-project/maven-play2-plugin/issues/13/labels{/name}",
     "comments_url": "https://api.github.com/repos/nanoko-project/maven-play2-plugin/issues/13/comments",
     "events_url": "https://api.github.com/repos/nanoko-project/maven-play2-plugin/issues/13/events",
     "html_url": "https://github.com/nanoko-project/maven-play2-plugin/issues/13",
     "id": 18833997,
     "number": 13,
     "title": "Update servlet bridge to 1.0",
     "user": {
     "login": "cescoffier",
     "id": 402301,
     "avatar_url": "https://1.gravatar.com/avatar/ab937f7df990a673446f1e30fd9ccfba?d=https%3A%2F%2Fidenticons.github.com%2Fa546e5879c098471267004df4d5bbf42.png",
     "gravatar_id": "ab937f7df990a673446f1e30fd9ccfba",
     "url": "https://api.github.com/users/cescoffier",
     "html_url": "https://github.com/cescoffier",
     "followers_url": "https://api.github.com/users/cescoffier/followers",
     "following_url": "https://api.github.com/users/cescoffier/following{/other_user}",
     "gists_url": "https://api.github.com/users/cescoffier/gists{/gist_id}",
     "starred_url": "https://api.github.com/users/cescoffier/starred{/owner}{/repo}",
     "subscriptions_url": "https://api.github.com/users/cescoffier/subscriptions",
     "organizations_url": "https://api.github.com/users/cescoffier/orgs",
     "repos_url": "https://api.github.com/users/cescoffier/repos",
     "events_url": "https://api.github.com/users/cescoffier/events{/privacy}",
     "received_events_url": "https://api.github.com/users/cescoffier/received_events",
     "type": "User"
     },
     "labels": [

     ],
     "state": "closed",
     "assignee": {
     "login": "cescoffier",
     "id": 402301,
     "avatar_url": "https://1.gravatar.com/avatar/ab937f7df990a673446f1e30fd9ccfba?d=https%3A%2F%2Fidenticons.github.com%2Fa546e5879c098471267004df4d5bbf42.png",
     "gravatar_id": "ab937f7df990a673446f1e30fd9ccfba",
     "url": "https://api.github.com/users/cescoffier",
     "html_url": "https://github.com/cescoffier",
     "followers_url": "https://api.github.com/users/cescoffier/followers",
     "following_url": "https://api.github.com/users/cescoffier/following{/other_user}",
     "gists_url": "https://api.github.com/users/cescoffier/gists{/gist_id}",
     "starred_url": "https://api.github.com/users/cescoffier/starred{/owner}{/repo}",
     "subscriptions_url": "https://api.github.com/users/cescoffier/subscriptions",
     "organizations_url": "https://api.github.com/users/cescoffier/orgs",
     "repos_url": "https://api.github.com/users/cescoffier/repos",
     "events_url": "https://api.github.com/users/cescoffier/events{/privacy}",
     "received_events_url": "https://api.github.com/users/cescoffier/received_events",
     "type": "User"
     },
     "milestone": {
     "url": "https://api.github.com/repos/nanoko-project/maven-play2-plugin/milestones/1",
     "labels_url": "https://api.github.com/repos/nanoko-project/maven-play2-plugin/milestones/1/labels",
     "id": 416471,
     "number": 1,
     "title": "1.2.3",
     "description": null,
     "creator": {
     "login": "cescoffier",
     "id": 402301,
     "avatar_url": "https://1.gravatar.com/avatar/ab937f7df990a673446f1e30fd9ccfba?d=https%3A%2F%2Fidenticons.github.com%2Fa546e5879c098471267004df4d5bbf42.png",
     "gravatar_id": "ab937f7df990a673446f1e30fd9ccfba",
     "url": "https://api.github.com/users/cescoffier",
     "html_url": "https://github.com/cescoffier",
     "followers_url": "https://api.github.com/users/cescoffier/followers",
     "following_url": "https://api.github.com/users/cescoffier/following{/other_user}",
     "gists_url": "https://api.github.com/users/cescoffier/gists{/gist_id}",
     "starred_url": "https://api.github.com/users/cescoffier/starred{/owner}{/repo}",
     "subscriptions_url": "https://api.github.com/users/cescoffier/subscriptions",
     "organizations_url": "https://api.github.com/users/cescoffier/orgs",
     "repos_url": "https://api.github.com/users/cescoffier/repos",
     "events_url": "https://api.github.com/users/cescoffier/events{/privacy}",
     "received_events_url": "https://api.github.com/users/cescoffier/received_events",
     "type": "User"
     },
     "open_issues": 0,
     "closed_issues": 11,
     "state": "open",
     "created_at": "2013-09-01T08:36:14Z",
     "updated_at": "2013-09-01T17:32:56Z",
     "due_on": null
     },
     "comments": 0,
     "created_at": "2013-09-01T08:36:14Z",
     "updated_at": "2013-09-01T09:26:33Z",
     "closed_at": "2013-09-01T09:26:33Z",
     "pull_request": {
     "html_url": null,
     "diff_url": null,
     "patch_url": null
     },
     "body": ""
     },
     */

}

/*
 * Copyright (C) 2024 - present Instructure, Inc.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package com.instructure.pandautils.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.instructure.canvasapi2.models.Attachment

class FileDownloader(private val context: Context) {
    fun downloadFileToDevice(attachment: Attachment) {
        downloadFileToDevice(attachment.url, attachment.filename, attachment.contentType)
    }

    fun downloadFileToDevice(
        downloadURL: String?,
        filename: String?,
        contentType: String?
    ) {
        downloadFileToDevice(Uri.parse(downloadURL), filename, contentType)
    }

    fun downloadFileToDevice(
        downloadURI: Uri,
        filename: String?,
        contentType: String?
    ) {
        val downloadManager = context.getSystemService(DownloadManager::class.java)

        val request = DownloadManager.Request(downloadURI)
        request
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle(filename)
            .setMimeType(contentType)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$filename")

        downloadManager.enqueue(request)
    }
}
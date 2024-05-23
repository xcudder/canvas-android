/*
 * Copyright (C) 2024 - present Instructure, Inc.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, version 3 of the License.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.instructure.parentapp.util

import androidx.hilt.work.HiltWorkerFactory
import com.instructure.canvasapi2.AppManager
import com.instructure.canvasapi2.utils.RemoteConfigUtils
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class AppManager : AppManager() {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        init()
    }

    override fun performLogoutOnAuthError() {
        // TODO: Implement
    }

    override fun getWorkManagerFactory() = workerFactory

    private fun init() {
        RemoteConfigUtils.initialize()
    }
}
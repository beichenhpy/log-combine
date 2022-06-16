/*
 * Copyright 2022 韩鹏宇
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package cn.beichenhpy.log.entity;

/**
 * <PRE>
 * 日志信息
 * </PRE>
 * CREATE_TIME: 2022/4/23 15:50
 *
 * @author beichenhpy
 * @version 1.0.0
 */

public class LogInfo {

    /**
     * 消息
     */
    private String message;

    /**
     * 递归层数
     */
    private int nestedFloor;


    public LogInfo(String message, int nestedFloor) {
        this.message = message;
        this.nestedFloor = nestedFloor;
    }


    public LogInfo() {
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getNestedFloor() {
        return nestedFloor;
    }

    public void setNestedFloor(int nestedFloor) {
        this.nestedFloor = nestedFloor;
    }
}

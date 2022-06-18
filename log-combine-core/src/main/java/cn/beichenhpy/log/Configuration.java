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

package cn.beichenhpy.log;


/**
 * 配置文件
 *
 * @author beichenhpy
 * @version 1.0.0
 */
public class Configuration {

    private String pattern;


    public Configuration(String pattern) {
        this.pattern = pattern;
    }

    public Configuration() {
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Configuration)) {
            return false;
        } else {
            Configuration other = (Configuration) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$pattern = this.getPattern();
                Object other$pattern = other.getPattern();
                if (this$pattern == null) {
                    if (other$pattern != null) {
                        return false;
                    }
                } else if (!this$pattern.equals(other$pattern)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof Configuration;
    }

    public int hashCode() {
        int result = 1;
        Object $pattern = this.getPattern();
        result = result * 59 + ($pattern == null ? 43 : $pattern.hashCode());
        return result;
    }

    public String toString() {
        return "Configuration(pattern=" + this.getPattern() + ")";
    }

}

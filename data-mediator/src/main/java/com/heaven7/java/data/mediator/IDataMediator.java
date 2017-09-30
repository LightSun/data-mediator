/**
 * Copyright 2017
 group of data-mediator
 member: heaven7(donshine723@gmail.com)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.heaven7.java.data.mediator;

import java.io.Serializable;

/**
 *  a super interface of 'data-mediator', which extends all our interface of 'data-mediator'(except parceable).
 * Created by heaven7 on 2017/9/11 0011.
 */
public interface IDataMediator extends Serializable, ICopyable, IResetable, IShareable, ISnapable{
}

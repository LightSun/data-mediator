package com.heaven7.java.data.mediator;

import java.io.Serializable;

/**
 *  a super interface of 'data-mediator', which extends all our interface of 'data-mediator'(except parceable).
 * Created by heaven7 on 2017/9/11 0011.
 */
public interface IDataMediator extends Serializable, ICopyable, IResetable, IShareable, ISnapable{
}

package org.auto.comet.example.chat.web.view;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: xiaohanghu
 * Date: 11-7-30
 * Time: ����5:45
 * To change this template use File | Settings | File Templates.
 */
public interface ResourceSerializer {

    Object serialization(Serializable resource);

}

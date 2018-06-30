package heaven7.test_compiler;

import com.heaven7.java.data.mediator.BaseMediator;
import com.heaven7.java.data.mediator.FamilyManager;
import com.heaven7.java.data.mediator.Property;

/**
 * Created by heaven7 on 2018/6/30 0030.
 */
public class ConnectorImpl implements FamilyManager.Connector {
    @Override
    public String connect(BaseMediator<?> mediator, Property main, Property slave) {
        return "";
    }
}

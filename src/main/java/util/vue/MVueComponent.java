package util.vue;

import lombok.Data;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lixiewen
 * @create 2019-12-24 9:38
 */
@Data
public class MVueComponent {
    private String name;
    private String fullName;
    /**
     * 必须放在这些组件之前,存的是组件的fullName
     */
    private List<String> befores = new ArrayList<>();
    private Element template;
    private Element script;
    private Element style;

    public void addBefores (String fullName){
        befores.add(fullName);
    }

}

package cloud.ptl.itemserver.service.implementation;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeneralService {
    public Boolean isImplementing(Object object, Class<?> clazz){
        return Arrays.asList(
                object.getClass().getInterfaces()
        ).contains(clazz);
    }

    public Boolean isImplementing(List<Object> objects, Class<?> clazz){
        return this.isImplementing(objects.get(0), clazz);
    }

    public List<?> castTo(List<Object> objects, Class<?> clazz){
        return objects.stream()
                .map(clazz::cast)
                .collect(Collectors.toList());
    }
}

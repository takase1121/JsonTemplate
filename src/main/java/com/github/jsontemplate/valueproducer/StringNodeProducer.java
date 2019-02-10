package com.github.jsontemplate.valueproducer;

import com.github.jsontemplate.jsonbuild.JsonStringNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class StringNodeProducer extends AbstractNodeProducer<JsonStringNode> {

    private final static String ALPHABETIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private final static int DEFAULT_LENGTH = 5;

    @Override
    public JsonStringNode produce() {
        return new JsonStringNode(() -> produceString(getDefaultLength()));
    }

    @Override
    public JsonStringNode produce(String value) {
        return new JsonStringNode(() -> value);
    }

    @Override
    public JsonStringNode produce(List<String> valueList) {
        return new JsonStringNode(() -> valueList.get(new Random().nextInt(valueList.size())));
    }

    @Override
    public JsonStringNode produce(Map<String, String> paramMap) {
        Map<String, String> copyParamMap = new HashMap<>(paramMap);

        Integer size = pickIntegerParam(copyParamMap, "size");
        Integer min = pickIntegerParam(copyParamMap, "min");
        Integer max = pickIntegerParam(copyParamMap, "max");

        validateParamMap(copyParamMap);

        if (size != null) {
            return new JsonStringNode(() -> produceString(size));
        } else if (min != null && max != null) {
            return new JsonStringNode(() -> produceString(randomIntInRange(min, max)));
        } else if (min != null) { // max == null
            return new JsonStringNode(() -> produceString(randomIntInRange(min, getDefaultMax(min))));
        } else if (max != null) { // min == null
            return new JsonStringNode(() -> produceString(randomIntInRange(getDefaultMin(max), max)));
        } else { // no expected parameters
            return produce();
        }
    }

    protected int getDefaultLength() {
        return DEFAULT_LENGTH;
    }

    protected int getDefaultMax(int min) {
        return 2 * min;
    }

    protected int getDefaultMin(int max) {
        return 0;
    }

    public String produceString(int length) {
        char[] chars = new char[length];
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(ALPHABETIC.length());
            chars[i] = ALPHABETIC.charAt(index);
        }
        return new String(chars);
    }
}

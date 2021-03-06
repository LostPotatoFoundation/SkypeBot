package Configuration;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**JavaDoc Author
 * @author  DrZed/KeldonSlayer/Keldon Yahne
 * I am by far not experienced with writing these,
 * I'm just doing it for boredom and since I fixed the API
 * to work properly, (for maps haven't fully tested lists)
 *
 * Original API Author
 * @author karelmikie3
 */

@SuppressWarnings("unchecked")
public class Category {
    private String name;
    private List<Setting> settings = new ArrayList<>();
    private String comment;

    /**
     * @param name the name of the Category
     */
    public Category(String name) {
        this.name = name;
        this.comment = "";
    }

    /**
     * @param name the name of the Category
     * @param comment the Comment under the name of the Category
     */
    public Category(String name, String comment) {
        this.name = name;
        this.comment = comment;
    }

    /**
     * @param setting the setting stored in a category
     */
    public Category addSetting(Setting setting) {
        settings.add(setting);
        return this;
    }

    /**
     * @return the name of the category
     */
    public String getName() {
        return name;
    }

    /**
     * @param clazz class for the data to be written to
     * @param reader the reader to read the data with
     */
    public void read(Class<?> clazz, BufferedReader reader) throws IOException {
        String line;
        while((line = reader.readLine()) != null) {
            try {
                if (line.equals("}")) return;
                if (line.contains("#")) continue;

                if (line.contains(":")) {
                    if (line.contains("<") && !line.contains("=")) {
                        readList(line, clazz, reader);
                    } else if (line.contains("[") && !line.contains("=")) {
                        readMap(line, clazz, reader);
                    } else {
                        readNormalVariable(line, clazz);
                    }
                }
            } catch (NumberFormatException | NoSuchFieldException | IllegalAccessException | NullPointerException | ClassCastException ignored) {}
        }
    }

    /**
     * @param line the line to read data from
     * @param clazz the class to write the data to
     */
    public void readNormalVariable(String line, Class<?> clazz) throws NoSuchFieldException, IllegalAccessException, NumberFormatException, NullPointerException {
        boolean hasNotEncounteredText = true;
        boolean hasEncounteredColumn = false;
        boolean hasEncounteredEquals = false;
        char[] chars = line.toCharArray();
        String type = "";
        StringBuilder variableNameBuilder = new StringBuilder();
        StringBuilder contentBuilder = new StringBuilder();
        for (Character character : chars) {
            if (!character.equals(' ') && !character.equals('\t') && hasNotEncounteredText) {
                hasNotEncounteredText = false;
                type = character.toString();
            } else if (character.equals(':')) {
                hasEncounteredColumn = true;
            } else if (!character.equals(' ') && !character.equals('\t') && hasEncounteredColumn && character.equals('=')) {
                hasEncounteredEquals = true;
            } else if (!character.equals(' ') && !character.equals('\t') && hasEncounteredColumn && !hasEncounteredEquals) {
                variableNameBuilder.append(character);
            } else if (hasEncounteredColumn && hasEncounteredEquals) {
                contentBuilder.append(character);
            }
        }

        String variableName = variableNameBuilder.toString();
        String contents = contentBuilder.toString();

        switch (type) {
            case "I":
                if(clazz.getField(variableName).getAnnotation(Config.Integer.class).forceReset() && Integer.parseInt((String) clazz.getField(variableName).get(clazz)) != 0) return;
                clazz.getField(variableName).set(clazz, Integer.parseInt(contents));
                break;
            case "S":
                if(clazz.getField(variableName).getAnnotation(Config.String.class).forceReset() && !(clazz.getField(variableName).get(clazz)).equals("")) return;
                clazz.getField(variableName).set(clazz, contents);
                break;
            case "B":
                if(clazz.getField(variableName).getAnnotation(Config.Boolean.class).forceReset() && !Boolean.parseBoolean((String) clazz.getField(variableName).get(clazz))) return;
                clazz.getField(variableName).set(clazz, Boolean.parseBoolean(contents));
                break;
            case "L":
                if(clazz.getField(variableName).getAnnotation(Config.Long.class).forceReset() && Long.parseLong((String) clazz.getField(variableName).get(clazz)) != 0L) return;
                clazz.getField(variableName).set(clazz, Long.parseLong(contents));
                break;
            case "F":
                if(clazz.getField(variableName).getAnnotation(Config.Float.class).forceReset() && Float.parseFloat((String) clazz.getField(variableName).get(clazz)) != 0F) return;
                clazz.getField(variableName).set(clazz, Float.parseFloat(contents));
                break;
            case "D":
                if(clazz.getField(variableName).getAnnotation(Config.Double.class).forceReset() && Double.parseDouble((String) clazz.getField(variableName).get(clazz)) != 0D) return;
                clazz.getField(variableName).set(clazz, Double.parseDouble(contents));
                break;
            case "C":
                if(clazz.getField(variableName).getAnnotation(Config.Character.class).forceReset() && !(clazz.getField(variableName).get(clazz)).equals(' ')) return;
                clazz.getField(variableName).set(clazz, contents);
                break;
            default: throw new NullPointerException("No correct type.");
        }
    }

    /**
     * @param line the line to read data from.
     * @param clazz the class to write the data to.
     * @param reader the reader to read the line with.
     */
    public <K,V> void readMap(String line, Class<?> clazz, BufferedReader reader) throws NoSuchFieldException, IllegalAccessException, IOException, NumberFormatException, NullPointerException, ClassCastException {
        boolean hasNotEncounteredText = true;
        boolean hasEncounteredColumn = false;
        char[] chars = line.toCharArray();
        Character prevChar = ' ';
        Map<String, String> values = new LinkedHashMap<>();
        String keyType = "";
        String valueType = "";
        StringBuilder variableNameBuilder = new StringBuilder();

        for (Character character : chars) {
            if (!character.equals(' ') && !character.equals('\t') && hasNotEncounteredText) {
                hasNotEncounteredText = false;
                keyType = character.toString();
            } else if (prevChar.equals('-')) {
                valueType = character.toString();
            } else if (character.equals(':')) {
                hasEncounteredColumn = true;
            } else if (!character.equals(' ') && !character.equals('\t') && hasEncounteredColumn) {
                variableNameBuilder.append(character.toString());
            } else if (character.equals(' ') && !hasNotEncounteredText && hasEncounteredColumn) {
                break;
            }
            prevChar = character;
        }
        String variableName = variableNameBuilder.toString();

        if(clazz.getField(variableName).getAnnotation(Config.Map.class).forceReset() && clazz.getField(variableName).get(clazz) != null) return;

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("\t]")) break;
            chars = line.toCharArray();
            boolean hasEncounteredEquals = false;
            StringBuilder keyBuilder = new StringBuilder();
            StringBuilder valueBuilder = new StringBuilder();

            for (Character character : chars) {
                if (character.equals('=') && !hasEncounteredEquals) {
                    hasEncounteredEquals = true;
                } else if (!character.equals('\t') && !hasEncounteredEquals) {
                    keyBuilder.append(character);
                } else if (!character.equals('\t') && hasEncounteredEquals) {
                    valueBuilder.append(character);
                }
            }
            String key = keyBuilder.toString();
            String value = valueBuilder.toString();

            if (!key.equals("")) values.put(key, value);
        }

        Class<K> keyType1 = (Class<K>) getTypeFromLetter(keyType);
        Class<V> valueType1 = (Class<V>) getTypeFromLetter(valueType);

        Map<K, V> map = (Map<K, V>) clazz.getField(variableName).get(clazz);
        final char a = keyType.charAt(0), b = valueType.charAt(0);
        values.entrySet().forEach(entry -> map.put(keyType1.cast(getVal(a, entry.getKey())), valueType1.cast(getVal(b, entry.getValue()))));
        clazz.getField(variableName).set(clazz, map);
    }

    /**
     * @param line the line to read data from.
     * @param clazz the class to write the data to.
     * @param reader the reader to read the line with.
     */
    public <V> void readList(String line, Class<?> clazz, BufferedReader reader) throws IOException, NoSuchFieldException, IllegalAccessException, NumberFormatException, NullPointerException, ClassCastException {
        boolean hasNotEncounteredText = true;
        boolean hasEncounteredColumn = false;
        char[] chars = line.toCharArray();
        List<String> values = new ArrayList<>();
        String type = "";
        StringBuilder variableNameBuilder = new StringBuilder();

        for (Character character : chars) {
            if (!character.equals(' ') && !character.equals('\t') && hasNotEncounteredText) {
                hasNotEncounteredText = false;
                type = character.toString();
            } else if (character.equals(':')) {
                hasEncounteredColumn = true;
            } else if (!character.equals(' ') && !character.equals('\t') && hasEncounteredColumn) {
                variableNameBuilder.append(character.toString());
            } else if (character.equals(' ') && !hasNotEncounteredText && hasEncounteredColumn) {
                break;
            }
        }
        String variableName = variableNameBuilder.toString();

        if(clazz.getField(variableName).getAnnotation(Config.List.class).forceReset() && clazz.getField(variableName).get(clazz) != null) return;

        while ((line = reader.readLine()) != null) {
            if (line.contains("\t>")) break;
            chars = line.toCharArray();
            StringBuilder valueBuilder = new StringBuilder();
            for (Character character : chars) {
                if (!character.equals('\t')) {
                    valueBuilder.append(character);
                }
            }
            String value = valueBuilder.toString();

            if (!value.equals("")) values.add(value);
        }

        Class<V> type1 = (Class<V>) getTypeFromLetter(type);
        List<V> list = values.stream().map(type1::cast).collect(Collectors.toList());
        clazz.getField(variableName).set(clazz, list);
    }

    /**
     * @param stringBuilder the StringBuilder to use to create the string
     */
    public StringBuilder write(StringBuilder stringBuilder) {
        stringBuilder.append(StringUtils.repeat('#', 106)).append("\n");
        stringBuilder.append("# ").append(name).append("\n");
        stringBuilder.append(StringUtils.repeat('#', 106)).append("\n");

        if (!comment.equals("")) {
            stringBuilder.append("# ").append(comment).append("\n");
            stringBuilder.append(StringUtils.repeat('#', 106)).append("\n").append("\n");
        } else stringBuilder.append("\n");


        stringBuilder.append(name).append(" {\n");

        Iterator<Setting> iterator = settings.iterator();
        while(iterator.hasNext()){
            Setting setting = iterator.next();

            if(!setting.getComment().equals("")) stringBuilder.append("\t# ").append(setting.getComment()).append("\n");
            if(setting.getType() == Setting.Type.STRING) {
                Setting<String> aSetting = (Setting<String>) setting;
                stringBuilder.append("\tS:").append(aSetting.getName()).append("=").append(aSetting.getValue()).append("\n");
            } else if(setting.getType() == Setting.Type.INTEGER) {
                Setting<Integer> aSetting = (Setting<Integer>) setting;
                stringBuilder.append("\tI:").append(aSetting.getName()).append("=").append(aSetting.getValue()).append("\n");
            } else if(setting.getType() == Setting.Type.BOOLEAN) {
                Setting<Boolean> aSetting = (Setting<Boolean>) setting;
                stringBuilder.append("\tB:").append(aSetting.getName()).append("=").append(aSetting.getValue()).append("\n");
            } else if(setting.getType() == Setting.Type.LIST) {
                Setting<List> aSetting = (Setting<List>) setting;
                stringBuilder.append("\t").append(getTypeLetterForList(aSetting.getValue())).append(":").append(aSetting.getName()).append(" <\n");
                for(Object object : aSetting.getValue()) stringBuilder.append("\t\t").append(object).append("\n");
                stringBuilder.append("\t>\n");
            } else if(setting.getType() == Setting.Type.MAP) {
                Setting<Map> aSetting = (Setting<Map>) setting;
                stringBuilder.append("\t").append(getTypeLettersForMap(aSetting.getValue())).append(":").append(aSetting.getName()).append(" [\n");
                for(Object object : aSetting.getValue().keySet()) stringBuilder.append("\t\t").append(object).append("=").append(aSetting.getValue().get(object)).append("\n");
                stringBuilder.append("\t]\n");
            } else if(setting.getType() == Setting.Type.LONG) {
                Setting<Long> aSetting = (Setting<Long>) setting;
                stringBuilder.append("\tL:").append(aSetting.getName()).append("=").append(aSetting.getValue()).append("\n");
            } else if(setting.getType() == Setting.Type.FLOAT) {
                Setting<Float> aSetting = (Setting<Float>) setting;
                stringBuilder.append("\tF:").append(aSetting.getName()).append("=").append(aSetting.getValue()).append("\n");
            } else if(setting.getType() == Setting.Type.DOUBLE) {
                Setting<Double> aSetting = (Setting<Double>) setting;
                stringBuilder.append("\tD:").append(aSetting.getName()).append("=").append(aSetting.getValue()).append("\n");
            } else if(setting.getType() == Setting.Type.CHARACTER) {
                Setting<String> aSetting = (Setting<String>) setting;
                stringBuilder.append("\tC:").append(aSetting.getName()).append("=").append(aSetting.getValue()).append("\n");
            }
            if(iterator.hasNext()) stringBuilder.append("\n");
        }

        stringBuilder.append("}\n\n\n");
        return stringBuilder;
    }

    /**
     * @param letter the character that specifies the stored data type
     *               it declares the type of data to return
     */
    private Class<?> getTypeFromLetter(String letter) {
        switch (letter) {
            case "S": return String.class;
            case "I": return Integer.class;
            case "B": return Boolean.class;
            case "L": return Long.class;
            case "F": return Float.class;
            case "D": return Double.class;
            case "C": return Character.class;
            default: return Object.class;
        }
    }

    /**
     * @param letter the character that specifies the stored data type
     *               it declares the type of data to return
     * @param val the string to process to convert to data type
     */
    private Object getVal(char letter, String val) {
        switch (letter) {
            case 'S': return String.valueOf(val);
            case 'I': return Integer.parseInt(val);
            case 'B': return Boolean.parseBoolean(val);
            case 'L': return Long.parseLong(val);
            case 'F': return Float.parseFloat(val);
            case 'D': return Double.parseDouble(val);
            case 'C': return val.charAt(0);
            default: return Object.class;
        }
    }

    /**
     * @param list the list to get the data type for
     */
    private String getTypeLetterForList(List list) {
        if (list.get(0) instanceof String) return "S";
        if (list.get(0) instanceof Integer) return "I";
        if (list.get(0) instanceof Boolean) return "B";
        if (list.get(0) instanceof Long) return "L";
        if (list.get(0) instanceof Float) return "F";
        if (list.get(0) instanceof Double) return "D";
        if (list.get(0) instanceof Character) return "C";
        return null;
    }


    /**
     * @param map to get the data types for
     */
    private String getTypeLettersForMap(Map map) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object object : map.keySet()) {
            if (object instanceof String) {
                stringBuilder.append("S");
                break;
            } else if (object instanceof Integer) {
                stringBuilder.append("I");
                break;
            } else if (object instanceof Boolean) {
                stringBuilder.append("B");
                break;
            } else if (object instanceof Long) {
                stringBuilder.append("L");
                break;
            } else if (object instanceof Float) {
                stringBuilder.append("F");
                break;
            } else if (object instanceof Double) {
                stringBuilder.append("D");
                break;
            } else if (object instanceof Character) {
                stringBuilder.append("C");
                break;
            }
        }
        stringBuilder.append("-");
        for(Object object : map.values()) {
            if (object instanceof String) {
                stringBuilder.append("S");
                break;
            } else if (object instanceof Integer) {
                stringBuilder.append("I");
                break;
            } else if (object instanceof Boolean) {
                stringBuilder.append("B");
                break;
            } else if (object instanceof Long) {
                stringBuilder.append("L");
                break;
            } else if (object instanceof Float) {
                stringBuilder.append("F");
                break;
            } else if (object instanceof Double) {
                stringBuilder.append("D");
                break;
            } else if (object instanceof Character) {
                stringBuilder.append("C");
                break;
            }
        }
        return stringBuilder.toString();
    }
}

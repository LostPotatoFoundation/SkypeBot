package Configuration;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * {} categories
 * # comments
 * <> lists
 * [] maps
 */

/**JavaDoc Author
 * @author  DrZed/KeldonSlayer/Keldon Yahne
 * I am by far not experienced with writing these,
 * I'm just doing it for boredom and since I fixed the API
 * to work properly, (for maps haven't fully tested lists)
 *
 * Original API Author
 * @author karelmikie3
 */
public class HxCConfig {
    private Map<String, Category> categories = new LinkedHashMap<>();
    private boolean hasRun = false;

    /**
     * @param clazz the class to be handled
     *              (read from and wrote to)
     * @param file the file to be processed
     */
    public void handleConfig(Class<?> clazz, File file) {
        if(!hasRun) {
            hasRun = true;
            for(Field field : clazz.getDeclaredFields()) {
                for(Annotation annotation : field.getAnnotations()) {
                    if (annotation.annotationType() == Config.String.class) handleString(clazz, field, (Config.String) annotation);
                    else if (annotation.annotationType() == Config.Integer.class) handleInteger(clazz, field, (Config.Integer) annotation);
                     else if (annotation.annotationType() == Config.Boolean.class) handleBoolean(clazz, field, (Config.Boolean) annotation);
                     else if (annotation.annotationType() == Config.List.class) handleList(clazz, field, (Config.List) annotation);
                     else if (annotation.annotationType() == Config.Map.class) handleMap(clazz, field, (Config.Map) annotation);
                     else if (annotation.annotationType() == Config.Long.class) handleLong(clazz, field, (Config.Long) annotation);
                     else if (annotation.annotationType() == Config.Float.class) handleFloat(clazz, field, (Config.Float) annotation);
                     else if (annotation.annotationType() == Config.Double.class) handleDouble(clazz, field, (Config.Double) annotation);
                     else if (annotation.annotationType() == Config.Character.class) handleCharacter(clazz, field, (Config.Character) annotation);
                }
            }
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("{")) {
                    char[] chars = line.toCharArray();
                    boolean hasNotEncounteredText = true;
                    StringBuilder stringBuilder = new StringBuilder();
                    for(Character character : chars) {
                        if((!character.equals(' ') && !character.equals('\t')) || hasNotEncounteredText) {
                            hasNotEncounteredText = false;
                            stringBuilder.append(character);
                        } else if(character.equals(' ')) break;
                    }
                    categories.getOrDefault(stringBuilder.toString(), categories.get("General")).read(clazz, reader);
                }
            }
        } catch (IOException ignored) {}

        StringBuilder stringBuilder = new StringBuilder();
        categories.values().forEach(category -> category.write(stringBuilder));
        String fileString = stringBuilder.toString();

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
            writer.append(fileString);
        } catch (IOException ignored) {}
    }

    /**
     * @param clazz the class to be handled
     *              (read from and wrote to)
     * @param field the field to be processed
     * @param annotation the annotation present
     */
    private void handleString(Class<?> clazz, Field field, Config.String annotation) {
        if(!categories.keySet().contains(annotation.category())) categories.put(annotation.category(), new Category(annotation.category(), ""));
        Setting<String> setting = new Setting<>(clazz, annotation.description(), field, Setting.Type.STRING, field.getName(), annotation.validValues());
        categories.put(annotation.category(), categories.get(annotation.category()).addSetting(setting));
    }

    /**
     * @param clazz the class to be handled
     *              (read from and wrote to)
     * @param field the field to be processed
     * @param annotation the annotation present
     */
    private void handleInteger(Class<?> clazz, Field field, Config.Integer annotation) {
        if(!categories.keySet().contains(annotation.category())) categories.put(annotation.category(), new Category(annotation.category(), ""));
        Setting<Integer> setting = new Setting<>(clazz, annotation.description(), field, Setting.Type.INTEGER, field.getName(), annotation.minValue(), annotation.maxValue());
        categories.put(annotation.category(), categories.get(annotation.category()).addSetting(setting));
    }

    /**
     * @param clazz the class to be handled
     *              (read from and wrote to)
     * @param field the field to be processed
     * @param annotation the annotation present
     */
    private void handleBoolean(Class<?> clazz, Field field, Config.Boolean annotation) {
        if(!categories.keySet().contains(annotation.category())) categories.put(annotation.category(), new Category(annotation.category(), ""));
        Setting<Boolean> setting = new Setting<>(clazz, annotation.description(), field, Setting.Type.BOOLEAN, field.getName());
        categories.put(annotation.category(), categories.get(annotation.category()).addSetting(setting));
    }

    /**
     * @param clazz the class to be handled
     *              (read from and wrote to)
     * @param field the field to be processed
     * @param annotation the annotation present
     */
    private void handleList(Class<?> clazz, Field field, Config.List annotation) {
        if(!categories.keySet().contains(annotation.category())) categories.put(annotation.category(), new Category(annotation.category(), ""));
        Setting<List> setting = new Setting<>(clazz, annotation.description(), field, Setting.Type.LIST, field.getName());
        categories.put(annotation.category(), categories.get(annotation.category()).addSetting(setting));
    }

    /**
     * @param clazz the class to be handled
     *              (read from and wrote to)
     * @param field the field to be processed
     * @param annotation the annotation present
     */
    private void handleMap(Class<?> clazz, Field field, Config.Map annotation) {
        if(!categories.keySet().contains(annotation.category())) categories.put(annotation.category(), new Category(annotation.category(), ""));
        Setting<Map> setting = new Setting<>(clazz, annotation.description(), field, Setting.Type.MAP, field.getName());
        categories.put(annotation.category(), categories.get(annotation.category()).addSetting(setting));
    }

    /**
     * @param clazz the class to be handled
     *              (read from and wrote to)
     * @param field the field to be processed
     * @param annotation the annotation present
     */
    private void handleLong(Class<?> clazz, Field field, Config.Long annotation) {
        if(!categories.keySet().contains(annotation.category())) categories.put(annotation.category(), new Category(annotation.category(), ""));
        Setting<Long> setting = new Setting<>(clazz, annotation.description(), field, Setting.Type.LONG, field.getName(), annotation.minValue(), annotation.maxValue());
        categories.put(annotation.category(), categories.get(annotation.category()).addSetting(setting));
    }

    /**
     * @param clazz the class to be handled
     *              (read from and wrote to)
     * @param field the field to be processed
     * @param annotation the annotation present
     */
    private void handleFloat(Class<?> clazz, Field field, Config.Float annotation) {
        if(!categories.keySet().contains(annotation.category())) categories.put(annotation.category(), new Category(annotation.category(), ""));
        Setting<Float> setting = new Setting<>(clazz, annotation.description(), field, Setting.Type.FLOAT, field.getName(), annotation.minValue(), annotation.maxValue());
        categories.put(annotation.category(), categories.get(annotation.category()).addSetting(setting));
    }

    /**
     * @param clazz the class to be handled
     *              (read from and wrote to)
     * @param field the field to be processed
     * @param annotation the annotation present
     */
    private void handleDouble(Class<?> clazz, Field field, Config.Double annotation) {
        if(!categories.keySet().contains(annotation.category())) categories.put(annotation.category(), new Category(annotation.category(), ""));
        Setting<Double> setting = new Setting<>(clazz, annotation.description(), field, Setting.Type.DOUBLE, field.getName(), annotation.minValue(), annotation.maxValue());
        categories.put(annotation.category(), categories.get(annotation.category()).addSetting(setting));
    }

    /**
     * @param clazz the class to be handled
     *              (read from and wrote to)
     * @param field the field to be processed
     * @param annotation the annotation present
     */
    private void handleCharacter(Class<?> clazz, Field field, Config.Character annotation) {
        if(!categories.keySet().contains(annotation.category())) categories.put(annotation.category(), new Category(annotation.category(), ""));
        Setting<String> setting = new Setting<>(clazz, annotation.description(), field, Setting.Type.CHARACTER, field.getName());
        categories.put(annotation.category(), categories.get(annotation.category()).addSetting(setting));
    }

    /**
     * @param category the category to be registered
     */
    public void registerCategory(Category category) {
        if(!categories.keySet().contains(category.getName())) categories.put(category.getName(), category);
    }
}

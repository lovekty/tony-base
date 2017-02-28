package me.tony.base.orm.mybatis.generator.plugin.mysql;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;

/**
 * Created by tony on 2017/2/28.
 */
public class PagePlugin4MySQL extends PluginAdapter {
    private PrimitiveTypeWrapper intWrapper;

    public PagePlugin4MySQL() {
        intWrapper = FullyQualifiedJavaType.getIntInstance().getPrimitiveTypeWrapper();
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (introspectedTable.getTargetRuntime() == IntrospectedTable.TargetRuntime.MYBATIS3) {
            enhanceModelExampleForPage(topLevelClass);
        }
        return true;
    }

    /**
     * <if test="limit != null and limit > 0">
     * <choose>
     * <when test="offset != null and offset > 0">
     * limit ${offset}, ${limit}
     * </when>
     * <otherwise>
     * limit ${limit}
     * </otherwise>
     * </choose>
     * </if>
     *
     * @param element
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {

        if (introspectedTable.getTargetRuntime() == IntrospectedTable.TargetRuntime.MYBATIS3) {
            XmlElement ifLimitNotNullElement = new XmlElement("if");
            ifLimitNotNullElement.addAttribute(new Attribute("test", "limit != null and limit > 0"));

            XmlElement chooseElement = new XmlElement("choose");

            XmlElement whenElement = new XmlElement("when");
            whenElement.addAttribute(new Attribute("test", "offset != null and offset > 0"));
            whenElement.addElement(new TextElement("limit ${offset}, ${limit}"));

            XmlElement otherwiseElement = new XmlElement("otherwise");
            otherwiseElement.addElement(new TextElement("limit ${limit}"));

            chooseElement.addElement(whenElement);
            chooseElement.addElement(otherwiseElement);
            ifLimitNotNullElement.addElement(chooseElement);
            element.addElement(ifLimitNotNullElement);
        }
        return true;
    }

    private void enhanceModelExampleForPage(TopLevelClass topLevelClass) {
        addIntField(topLevelClass, "limit");
        addIntGetterAndSetter(topLevelClass, "limit");
        addIntField(topLevelClass, "offset");
        addIntGetterAndSetter(topLevelClass, "offset");
    }

    private void addIntField(TopLevelClass topLevelClass, String fieldName) {
        Field field = new Field();
        field.setName(fieldName);
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setType(intWrapper);
        topLevelClass.addField(field);
    }

    private void addIntGetterAndSetter(TopLevelClass topLevelClass, String fieldName) {
        String setterName = "set" + upperFirstChar(fieldName);
        Method setter = new Method();
        setter.setVisibility(JavaVisibility.PUBLIC);
        setter.setName(setterName);
        setter.addParameter(new Parameter(intWrapper, fieldName));
        setter.addBodyLine("this." + fieldName + " = " + fieldName + ";");
        topLevelClass.addMethod(setter);

        String getterName = "get" + upperFirstChar(fieldName);
        Method getter = new Method();
        getter.setVisibility(JavaVisibility.PUBLIC);
        getter.setReturnType(intWrapper);
        getter.setName(getterName);
        getter.addBodyLine("return this." + fieldName + ";");
        topLevelClass.addMethod(getter);
    }

    private String upperFirstChar(String str) {
        if (str == null || str.length() == 0) {
            throw new RuntimeException("String parameter need to be not empty, but this is " + String.valueOf(str));
        }
        if (str.length() == 1) {
            return str.toUpperCase();
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}

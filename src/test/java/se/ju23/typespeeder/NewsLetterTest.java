package se.ju23.typespeeder;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class NewsLetterTest {

    @Test
    public void testNewsLetterClassExists() {
        assertDoesNotThrow(() -> Class.forName("se.ju23.typespeeder.NewsLetter"));
    }

    @Test
    public void testNewsLetterContentLength() {
        try {
            Class<?> newsLetterClass = Class.forName("se.ju23.typespeeder.NewsLetter");

            Field contentField = newsLetterClass.getDeclaredField("content");
            assertNotNull(contentField, "Field 'content' should exist in NewsLetter.");

            assertTrue(contentField.getType().equals(String.class), "Field 'content' should be of type String.");

            Object instance = newsLetterClass.getDeclaredConstructor(String.class).newInstance("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");

            Method getterMethod = newsLetterClass.getDeclaredMethod("getContent");
            String contentValue = null;
            try {
                contentValue = (String) getterMethod.invoke(instance);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }

            assertTrue(contentValue.length() >= 100, "Content field length should be at least 100 characters.");
            assertTrue(contentValue.length() <= 200, "Content field length should be at most 200 characters.");

        } catch (ClassNotFoundException | NoSuchFieldException | NoSuchMethodException | IllegalAccessException e) {
            fail("Error occurred while testing NewsLetter content field length.", e);
        } catch (InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testNewsLetterPublishDateTime() {
        try {
            Class<?> someClass = Class.forName("se.ju23.typespeeder.NewsLetter");

            Field publishDateTime = someClass.getDeclaredField("publishDateTime");
            assertNotNull(publishDateTime, "Field 'publishDateTime' should exist in NewsLetter class.");

            assertTrue(publishDateTime.getType().equals(LocalDateTime.class), "Field 'publishDateTime' should be of type LocalDateTime.");

            Object instance = someClass.getDeclaredConstructor().newInstance();
            Method getterMethod = someClass.getDeclaredMethod("getPublishDateTime");
            LocalDateTime dateTimeValue = (LocalDateTime) getterMethod.invoke(instance);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = dateTimeValue != null ? dateTimeValue.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
            assertEquals("Expected format", formattedDateTime, "'publishDateTime' field should have format 'yyyy-MM-dd HH:mm:ss'.");

        } catch (ClassNotFoundException | NoSuchFieldException | NoSuchMethodException e) {
            fail("Error occurred while testing properties of NewsLetter.", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (java.lang.reflect.InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
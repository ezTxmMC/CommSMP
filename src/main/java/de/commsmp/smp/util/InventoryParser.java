package de.commsmp.smp.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Utility-Klasse zur Serialisierung und Deserialisierung von ItemStacks und ItemStack-Arrays in Base64.
 */
public class InventoryParser {

    /**
     * Serialisiert ein Array von {@link ItemStack} in einen Base64-codierten String.
     *
     * @param items Das ItemStack-Array, das serialisiert werden soll (darf nicht null sein).
     * @return Der Base64-codierte String.
     * @throws IllegalArgumentException wenn das Array null ist.
     */
    public static String itemStackArrayToBase64(ItemStack[] items) {
        if (items == null) {
            throw new IllegalArgumentException("Das ItemStack-Array darf nicht null sein.");
        }
        return encodeToBase64(dataOut -> {
            dataOut.writeInt(items.length);
            for (ItemStack item : items) {
                dataOut.writeObject(item);
            }
        });
    }

    /**
     * Serialisiert ein einzelnes {@link ItemStack} in einen Base64-codierten String.
     *
     * @param item Das ItemStack, das serialisiert werden soll (darf nicht null sein).
     * @return Der Base64-codierte String.
     * @throws IllegalArgumentException wenn das Item null ist.
     */
    public static String itemStackToBase64(ItemStack item) {
        if (item == null) {
            throw new IllegalArgumentException("Das ItemStack darf nicht null sein.");
        }
        return encodeToBase64(dataOut -> dataOut.writeObject(item));
    }

    /**
     * Deserialisiert einen Base64-codierten String in ein einzelnes {@link ItemStack}.
     *
     * @param data Der Base64-codierte String.
     * @return Das deserialisierte ItemStack.
     * @throws IOException wenn ein Fehler beim Deserialisieren auftritt oder das Objekt kein ItemStack ist.
     */
    public static ItemStack itemStackFromBase64(String data) throws IOException {
        Object decoded = decodeSingleFromBase64(data);
        if (decoded instanceof ItemStack stack) {
            return stack;
        }
        throw new IOException("Das decodierte Objekt ist kein ItemStack.");
    }

    /**
     * Deserialisiert einen Base64-codierten String in ein Array von {@link ItemStack}.
     *
     * @param data Der Base64-codierte String.
     * @return Das deserialisierte ItemStack-Array.
     * @throws IOException wenn ein Fehler beim Deserialisieren auftritt oder das Objekt kein ItemStack-Array ist.
     */
    public static ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
        ItemStack[] items = decodeArrayFromBase64(data);
        if (items != null) {
            return items;
        }
        throw new IOException("Das decodierte Objekt ist kein ItemStack-Array.");
    }

    /**
     * Encodiert Objekte in Base64.
     *
     * @param writer Eine Lambda-Funktion, die die Objekte in den OutputStream schreibt.
     * @return Der Base64-codierte String.
     * @throws IllegalStateException falls ein Fehler beim Schreiben auftritt.
     */
    private static String encodeToBase64(ThrowingConsumer<BukkitObjectOutputStream> writer) {
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOut = new BukkitObjectOutputStream(byteOut)) {

            writer.accept(dataOut);
            return Base64Coder.encodeLines(byteOut.toByteArray());

        } catch (Exception e) {
            throw new IllegalStateException("Fehler beim Encodieren der Items in Base64.", e);
        }
    }

    /**
     * Decodiert einen Base64-codierten String in ein einzelnes Objekt.
     *
     * @param data Der Base64-codierte String.
     * @return Das deserialisierte Objekt.
     * @throws IOException wenn ein Fehler beim Deserialisieren auftritt.
     */
    private static Object decodeSingleFromBase64(String data) throws IOException {
        try (ByteArrayInputStream byteIn = new ByteArrayInputStream(Base64Coder.decodeLines(data));
             BukkitObjectInputStream dataIn = new BukkitObjectInputStream(byteIn)) {

            return dataIn.readObject();

        } catch (ClassNotFoundException e) {
            throw new IOException("Klasse nicht gefunden während des Decodierens.", e);
        }
    }

    /**
     * Decodiert einen Base64-codierten String in ein Array von {@link ItemStack}.
     *
     * @param data Der Base64-codierte String.
     * @return Das deserialisierte ItemStack-Array.
     * @throws IOException wenn ein Fehler beim Deserialisieren auftritt.
     */
    private static ItemStack[] decodeArrayFromBase64(String data) throws IOException {
        try (ByteArrayInputStream byteIn = new ByteArrayInputStream(Base64Coder.decodeLines(data));
             BukkitObjectInputStream dataIn = new BukkitObjectInputStream(byteIn)) {

            int length = dataIn.readInt();
            ItemStack[] items = new ItemStack[length];
            for (int i = 0; i < length; i++) {
                items[i] = (ItemStack) dataIn.readObject();
            }
            return items;

        } catch (ClassNotFoundException e) {
            throw new IOException("Klasse nicht gefunden während des Decodierens.", e);
        }
    }

    /**
     * Funktionales Interface, um Checked Exceptions in Lambdas handhaben zu können.
     *
     * @param <T> Der Typ des Objekts, das verarbeitet wird.
     */
    @FunctionalInterface
    private interface ThrowingConsumer<T> {
        void accept(T t) throws Exception;
    }
}

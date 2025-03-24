package de.commsmp.smp.config.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LockInfo {

    private final String owner;
    private final List<String> trusted;
    private final boolean viewable;
    private final boolean donate;

    public LockInfo(String owner, List<String> trusted, boolean viewable, boolean donate) {
        this.owner = owner;
        this.trusted = trusted;
        this.viewable = viewable;
        this.donate = donate;
    }

    public String getOwner() {
        return owner;
    }

    public List<String> getTrusted() {
        return trusted;
    }

    public boolean isViewable() {
        return viewable;
    }

    public boolean isDonate() {
        return donate;
    }

    @Override
    public String toString() {
        return "LockInfo{" +
                "owner='" + owner + '\'' +
                ", trusted=" + trusted +
                ", viewable=" + viewable +
                ", donate=" + donate +
                '}';
    }

    public static LockInfo fromString(String input) {
        if (input == null || !input.startsWith("LockInfo{")) {
            throw new IllegalArgumentException("Ungültige LockInfo-String-Repräsentation");
        }

        try {
            String inner = input.substring("LockInfo{".length(), input.length() - 1);
            String[] parts = inner.split(", (?=trusted=|viewable=|donate=)");

            String owner = parts[0].split("=")[1].replace("'", "");

            String trustedRaw = parts[1].substring("trusted=".length());
            List<String> trusted;
            if (trustedRaw.equals("[]")) {
                trusted = new ArrayList<>();
            } else {
                trustedRaw = trustedRaw.substring(1, trustedRaw.length() - 1);
                trusted = new ArrayList<>(Arrays.asList(trustedRaw.split(", ")));
            }

            boolean viewable = Boolean.parseBoolean(parts[2].split("=")[1]);
            boolean donate = Boolean.parseBoolean(parts[3].split("=")[1]);

            return new LockInfo(owner, trusted, viewable, donate);
        } catch (Exception e) {
            throw new IllegalArgumentException("Fehler beim Parsen von LockInfo.fromString(): " + input, e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LockInfo)) return false;
        LockInfo that = (LockInfo) o;
        return viewable == that.viewable &&
                donate == that.donate &&
                Objects.equals(owner, that.owner) &&
                Objects.equals(trusted, that.trusted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, trusted, viewable, donate);
    }
}

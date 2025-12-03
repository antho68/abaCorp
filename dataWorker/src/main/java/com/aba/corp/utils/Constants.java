package com.aba.corp.utils;

import java.util.Collection;
import java.util.Map;

public interface Constants
{
    public interface BankRecordDataType
    {
        public final static String HOUSEHOLD = "1";
        public final static String HEALTH = "2";
        public final static String CAR = "3";
        public final static String INTERNET_MOBILE = "4";
        public final static String SAVES = "5";
        public final static String TAXES = "6";
        public final static String HOBBY = "7";

        public final static Collection<String> allTypes = java.util.Arrays.asList(HOUSEHOLD, HEALTH, CAR
                , INTERNET_MOBILE, SAVES, TAXES, HOBBY);
        public final static Map<String, String> labels = Map.of(
                HOUSEHOLD, "Ménage",
                HEALTH, "Santé",
                CAR, "Voiture",
                INTERNET_MOBILE, "Internet & Mobile",
                SAVES, "Epargne",
                TAXES, "Impôts",
                HOBBY, "Loisir"
        );
    }

    public interface BankRecordDataPaymentType
    {
        public final static String CB = "1";
        public final static String CB_THROW_SP = "2";
        public final static String VIRMENT = "3";
        public final static String PREV = "4";

        public final static String OTHER = "99";

        public final static Collection<String> all = java.util.Arrays.asList(CB, CB_THROW_SP, VIRMENT
                , PREV, OTHER);
        public final static Map<String, String> labels = Map.of(
                CB, "CB",
                CB_THROW_SP, "Apple Pay",
                VIRMENT, "Virement",
                PREV, "Prélèvement",
                OTHER, "Autre"
        );
    }

    public interface BankRecordDataOwner
    {
        public final static String ANTHO = "1";
        public final static String MALO = "2";
        public final static String COUPLE = "3";
        public final static String NOHAN = "4";
        public final static String RIO = "5";

        public final static Collection<String> all = java.util.Arrays.asList(ANTHO, MALO, COUPLE
                , NOHAN, RIO);
        public final static Map<String, String> labels = Map.of(
                ANTHO, "ANTHO",
                MALO, "MALO",
                COUPLE, "COUPLE",
                NOHAN, "NOHAN",
                RIO, "RIO"
        );
    }

    public interface BankRecordDataScope
    {
        public final static String INTERNAL = "1";
        public final static String EXTERNAL = "2";

        public final static Collection<String> all = java.util.Arrays.asList(INTERNAL, EXTERNAL);
        public final static Map<String, String> labels = Map.of(
                INTERNAL, "INTERNE",
                EXTERNAL, "EXTERNE"
        );
    }

    public interface UserRoleType
    {
        public final static String ADMIN = "ADMIN";
        public final static String SUP_USER = "SUP_USER";
        public final static String USER = "USER";

        public final static Collection<String> allRoles = java.util.Arrays.asList(ADMIN, SUP_USER, USER);
        public final static Map<String, String> roleLabels = Map.of(
                ADMIN, "Administrateur",
                SUP_USER, "Super Utilisateur",
                USER, "Utilisateur"
        );
    }

    public interface AccountType
    {
        public final static String ORDINARY = "ORDINARY";
        public final static String SAVING = "SAVING";
        public final static String LAON = "LAON";

        public final static Collection<String> allTypes = java.util.Arrays.asList(ORDINARY, SAVING, LAON);
        public final static Map<String, String> labels = Map.of(
                ORDINARY, "Ordinaire",
                SAVING, "Epargne",
                LAON, "Prêt"
        );
    }

}

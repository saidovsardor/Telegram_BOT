package uz.pdp.backend.utils;

import lombok.Getter;

@Getter

public enum MessageKey {
    BACK("back", "Orqaga"),
    INFORMATION_ERROR("information.error", """
            Siz kiritgan ma'lumotlar noto'g'ri!
            """.trim()),
    CHOOSE_LANGUAGE("choose.language", """
            Assalomu aleykum.
            """.trim()),
    SHARE_PHONE_NUMBER("share.phone.number", """
            Telefon raqamingizni kiriting.
            """.trim()),
    SHARE_PHONE_NUMBER_BUTTON("share.phone.number.button", """
            Telefon raqamni ulashish.
            """.trim()),
    MAIN_MENU("main.menu", """
            Asosiy menyu.
            """.trim()),
    SETTINGS_BUTTON("settings.button", """
            Sozlamalar.
            """.trim()),
    SETTINGS("settings", """
            Sozlamalar oynasi.
            """.trim()),
    ORDER_BUTTON("order.button", """
            Buyurtma berish.
            """.trim()),
    // properties yozish kerak
    SUBMIT_APPLICATION_BUTTON("submit.application.button", """
            Ariza yuborish.
            """.trim().trim()),
    // properties yozish kerak
    CHOOSE_APPLICATION_TYPE("choose.application.type", """
            Ariza turini tanlang.
            """.trim()),

    BUSINESS_APPLICATION_BUTTON("business.application.button", """
            Biznes
            """.trim()),
    // properties yozish kerak
    COURIER_APPLICATION_BUTTON("courier.application.button", """
            Courier
            """.trim()),
    BUSINESS_APPLICATION_ENTER_NAME("business.application.enter.name", """
            Biznes nomini kiriting.
            """.trim()),
    BUSINESS_APPLICATION_ENTER_TYPE("business.application.enter.type", """
            Biznes tipini kiriting.
            """.trim()),
    BUSINESS_APPLICATION_ENTER_LOCATION("business.application.enter.location", """
            Bizness lakatsiyasini kiriting.
            """.trim()),
    FINISHED_BUSINESS_APPLICATION("finished.business.application", """
            Ariza muvaffaqiyatli yuborildi
            """.trim()),
    // properties yozish kerak
    COURIER_APPLICATION_ENTER_BIRTH_DATE("courier.application.enter.birth.date", """
            Tu'gilgan kuningizni kiriting.
            Format = dd.MM.yyyy.
            """.trim()),
    SET_LANGUAGE_BUTTON("set.language.button", """
            Tilni o'zgartirish.
            """.trim()),
    SET_LANGUAGE("set.language", """
            Tilni o'zgartirish.
            """.trim()),

    SHARE_LOCATION_BUTTON("share.location.button", """
            lakatsiyani yuborish.
            """.trim()),

    // ------------------------------------------ADMIN MESSAGE KEY-------------------------------------------------,
    STARTED_ADMIN("", """
            Assalomu aleykum admin.
            %s!
            """.trim()),
    ADMIN_PANEL("admin.panel", """
            Admin panel.
            """.trim()),

    GET_ALL_APPLICATION_BUTTON("get.all.business.application.button", """
            Arizalarni ko'rish.
            """.trim()),

    SETTINGS_ADMIN("settings.admin", """
            Sozlamalar menyusi.
            """.trim()),
    ADD_CONTACT_ADMIN("add.contact.admin", """
            Telefon raqamni qo'shish menyusi
            """.trim()),
    CHANGE_CONTACT_BUTTON("change.contact.button", """
            Telefon raqamni o'zgartirish
            """.trim()),
    APPLICATIONS_ADMIN("applications.admin", """
            Arizalar ro'yxati:
            """.trim()),
    APPLICATIONS_TYPE_ADMIN("applications.type.admin", """
            Arizalar turi:
            """.trim()),
    WRONG_ROLE_ADMIN("wrong.role.admin", """
            Siz admin emassiz!
            """.trim()),
    OPEN_BUSINESS_APPLICATION("open.business.application", """
            Yuboruvchi:
                Full Name = %s
                User Name = @%s
                Phone Number = %s
            Biznesning Nomi = %s
            Biznesning Tipi = %s
            Ariza yuborilgan vaqt = %s
            """.trim()),
    ACCEPT_BUSINESS_APPLICATION("accept.business.location", """
            Qabul qilish.
            """.trim()),
    REJECT_BUSINESS_APPLICATION("reject.business.location", """
            Rad etish.
            """.trim()),
    WRITE_COMMENT_ADMIN("write.comment.admin", """
            Rad etish sababini kiriting:
            """.trim()),
    ACCEPTED_APPLICATION_ADMIN("accepted.application.admin", """
            Admin sizning arizangizni ko'rib chiqdi va qabul qildi.
            """.trim()),
    REJECTED_APPLICATION_ADMIN("rejected.application.admin", """
            Admin sizning arizangizni rad etdi!\nRad etish sababi: \n 
            """.trim()),
    BUSINESS_TYPE_APPLICATION_BUTTON("business.type.application.button", """
            Biznes
            """.trim()),
    COURIER_TYPE_APPLICATION_BUTTON("courier.type.application.button", """
            Kuryer
            """.trim()),
    GET_REPORT_BUTTON("get.report.button", """
            Hisobotlar
            """.trim()),
    REPORTS_ADMIN("reports.admin", """
            Foydalanuvchilar soni : %s
            Kuryerlar soni : %s
            Bizneslar soni : %s
            """.trim()),


    //---------------------------------------BUSINESSMAN MESSAGE KEY---------------------------------------

    ADD_PRODUCT_BUTTON("add.product.button", """
            product qo'shish.
            """.trim()),

    SETTINGS_BUSINESSMAN("settings.businessman", """
            sozlamalar menusi.
            """.trim()),

    BUSINESSMAN_PANEL("businessman.panel", """
            Businessman panel.
            """.trim()),


    ADD_PRODUCT_ENTER_CATEGORY("add.product.enter.category", """
            Categoryani tanlang yoki yanki categoryani kiriting.
            """.trim()),

    ADD_PRODUCT_ENTER_NAME("add.product.enter.name", """
            Maxsulot nomini kiriting.
            """.trim()),

    ADD_PRODUCT_ENTER_PRICE("add.product.enter.price", """
            Maxsulot narxini kiriting.
            """.trim()),

    ADD_PRODUCT_ENTER_AMOUNT("add.product.enter.amount", """
            Maxsulot sonini kiriting.
            """.trim()),

    ADD_PRODUCT_ENTER_PHOTO("add.product.enter.photo", """
            Maxsulot rasmini kiriting.
            """.trim()),

    ADD_PRODUCT_ENTER_DESCRIPTION("add.product.enter.description", """
            Maxsulotning qo'shimcha malumotlarini kiriting.
            """.trim()),

    ADD_PRODUCT_FINISHED("add.product.finished", """
            Maxsulot muvaffaqiyatli qo'shilidi.
            """.trim()),
    // properties yozish kk!
    ORDER_MENU("order.menu", """
            Buyurtma berish menusi.
            """.trim()),

    OPEN_BASKET_BUTTON("open.basket.button", """
            Savatni ko'rish.
            """.trim()),
    EDIT_INFORMATION_BUTTON("edit.information", """
            Ma'lumotlarni tahrirlash
            """.trim()),
    CHOOSE_TYPE_OF_EDITING("choose.type.of.editing", """
            Biznes haqida:
                Biznes nomi: %s
                Biznes turi: %s
                Hisob raqami: %s
                Biznes filiallari soni: %s
            """.trim()),
    EDIT_BUSINESS_NAME_BUTTON("edit.business.name.button", """
            Biznes nomini o'zgartirish
            """.trim()),
    EDIT_BUSINESS_LOCATION_BUTTON("edit.business.location.button", """
            Biznes joylashuvini qo'shish
            """.trim()),
    ENTER_NAME_FOR_BUSINESS("enter.name.for.business", """
            Biznes uchun nom kiriting:
            """.trim()),
    ENTER_LOCATION_FOR_BUSINESS("enter.location.for.business", """
            Biznes uchun lokatsiya kiriting:
            """.trim()),
    BUSINESS_NAME_EDITED("business.name.edited", """
            Biznes nomi o'zgartirildi!
            """.trim()),
    BUSINESS_LOCATION_EDITED("business.location.edited", """
            Biznes joylashuvi o'zgartirildi!
            """.trim()),
    NUMBER_OF_BUSINESS_LOCATIONS("number.of.business.locations", """
            Biznes filiallari soni: %s
            """.trim()),


    ADD_TO_BASKET_BUTTON("add.to.basket", """
            Savatga maxsulot qo'shish.
            """.trim()),
    CHOOSE_BUSINESS("choose.business", """
            Biznesni tanlang.
            """.trim()),

    CHOOSE_CATEGORY("choose.category", """
            Categoryani tanlang.
            """.trim())


    ;

    private String key;
    private String val;

    MessageKey(String key, String val) {
        this.key = key;
        this.val = val;
    }
}

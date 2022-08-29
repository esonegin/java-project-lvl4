package hexlet.code;

import io.ebean.Model;
import io.ebean.annotation.Platform;
import io.ebean.dbmigration.DbMigration;

import java.io.IOException;

public final class MigrationGenerator {

    public static void main(String[] args) throws IOException {
        io.ebean.dbmigration.DbMigration dbMigration = DbMigration.create();
        // Указываем платформу, в нашем случае H2
        dbMigration.addPlatform(Platform.H2, "h2");
        // Генерируем миграцию
        dbMigration.generateMigration();
    }
}

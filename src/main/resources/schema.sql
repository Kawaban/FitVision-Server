CREATE
    EXTENSION IF NOT EXISTS "uuid-ossp";

DROP
    SCHEMA IF EXISTS fitvision CASCADE;

CREATE
    SCHEMA fitvision;

DROP
    TABLE
        IF EXISTS fitvision.users;

CREATE
    TABLE
        fitvision.users(
            created_date TIMESTAMP(6) WITH TIME ZONE,
            last_modified_date TIMESTAMP(6) WITH TIME ZONE,
            version BIGINT NOT NULL DEFAULT 0,
            uuid UUID NOT NULL,
            email VARCHAR(255) UNIQUE NOT NULL,
            password VARCHAR(255) NOT NULL,
            is_enabled BOOLEAN DEFAULT FALSE NOT NULL,
            ROLE VARCHAR(10) NOT NULL,
            PRIMARY KEY(uuid)
        );

DROP
    TABLE
        IF EXISTS fitvision.users_info;

CREATE
    TABLE
        fitvision.users_info(
            created_date TIMESTAMP(6) WITH TIME ZONE,
            last_modified_date TIMESTAMP(6) WITH TIME ZONE,
            version BIGINT NOT NULL DEFAULT 0,
            uuid UUID NOT NULL,
            email VARCHAR(255) UNIQUE NOT NULL,
            name VARCHAR(255) NOT NULL,
            streak INT NOT NULL,
            longest_streak INT NOT NULL,
            completed_tasks INT NOT NULL,
            won_days INT NOT NULL,
            avatar bytea,
            PRIMARY KEY(uuid)
        );

DROP
    TABLE
        IF EXISTS fitvision.notifications;

CREATE
    TABLE
        fitvision.notifications(
            created_date TIMESTAMP(6) WITH TIME ZONE,
            last_modified_date TIMESTAMP(6) WITH TIME ZONE,
            version BIGINT NOT NULL DEFAULT 0,
            uuid UUID NOT NULL,
            TYPE VARCHAR(255) NOT NULL,
            email_sender VARCHAR(255) NOT NULL,
            email_recipient VARCHAR(255) NOT NULL,
            is_read BOOLEAN DEFAULT FALSE NOT NULL,
            notification_object_uuid UUID,
            PRIMARY KEY(uuid)
        );

DROP
    TABLE
        IF EXISTS fitvision.friend_requests;

CREATE
    TABLE
        fitvision.friend_requests(
            created_date TIMESTAMP(6) WITH TIME ZONE,
            last_modified_date TIMESTAMP(6) WITH TIME ZONE,
            version BIGINT NOT NULL DEFAULT 0,
            uuid UUID NOT NULL,
            sender_uuid UUID NOT NULL,
            receiver_uuid UUID NOT NULL,
            FOREIGN KEY(sender_uuid) REFERENCES fitvision.users_info(uuid),
            FOREIGN KEY(receiver_uuid) REFERENCES fitvision.users_info(uuid),
            PRIMARY KEY(uuid)
        );

DROP
    TABLE
        IF EXISTS fitvision.friends;

CREATE
    TABLE
        fitvision.friends(
            created_date TIMESTAMP(6) WITH TIME ZONE DEFAULT NOW(),
            last_modified_date TIMESTAMP(6) WITH TIME ZONE DEFAULT NOW(),
            version BIGINT NOT NULL DEFAULT 0,
            uuid UUID NOT NULL DEFAULT uuid_generate_v4(),
            user_id UUID NOT NULL,
            friend_id UUID NOT NULL,
            FOREIGN KEY(user_id) REFERENCES fitvision.users_info(uuid),
            FOREIGN KEY(friend_id) REFERENCES fitvision.users_info(uuid),
            PRIMARY KEY(uuid)
        );

DROP
    TABLE
        IF EXISTS fitvision.tasks;

CREATE
    TABLE
        fitvision.tasks(
            created_date TIMESTAMP(6) WITH TIME ZONE DEFAULT NOW(),
            last_modified_date TIMESTAMP(6) WITH TIME ZONE DEFAULT NOW(),
            version BIGINT NOT NULL DEFAULT 0,
            uuid UUID NOT NULL DEFAULT uuid_generate_v4(),
            user_uuid UUID NOT NULL,
            task_name VARCHAR(255) NOT NULL,
            task_image bytea,
            is_completed BOOLEAN DEFAULT FALSE NOT NULL,
            is_active BOOLEAN DEFAULT FALSE NOT NULL,
            is_prepared BOOLEAN DEFAULT FALSE NOT NULL,
            PRIMARY KEY(uuid),
            FOREIGN KEY(user_uuid) REFERENCES fitvision.users_info(uuid)
        );

DROP
    TABLE
        IF EXISTS fitvision.password_reset_tokens;

CREATE
    TABLE
        fitvision.password_reset_tokens(
            created_date TIMESTAMP(6) WITH TIME ZONE DEFAULT NOW(),
            last_modified_date TIMESTAMP(6) WITH TIME ZONE DEFAULT NOW(),
            version BIGINT NOT NULL DEFAULT 0,
            uuid UUID NOT NULL DEFAULT uuid_generate_v4(),
            user_uuid UUID NOT NULL,
            token VARCHAR(255) NOT NULL,
            expiration_date TIMESTAMP(6) WITH TIME ZONE NOT NULL,
            is_used BOOLEAN DEFAULT FALSE NOT NULL,
            PRIMARY KEY(uuid)
        );
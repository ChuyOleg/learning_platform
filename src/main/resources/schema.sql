CREATE TABLE IF NOT EXISTS roles (
    id serial PRIMARY KEY ,
    role varchar(16) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS user_details (
    id serial PRIMARY KEY ,
    money numeric NOT NULL ,
    tax_number varchar(16) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS persons (
    id serial PRIMARY KEY ,
    username varchar(32) UNIQUE NOT NULL ,
    password varchar(256) NOT NULL ,
    email varchar(128) NOT NULL ,
    birthday date NOT NULL ,
    blocked BOOLEAN NOT NULL DEFAULT FALSE,
    role_id int references roles(id) NOT NULL ,
    user_details_id int references user_details(id)
);

CREATE TABLE IF NOT EXISTS courses (
    id serial PRIMARY KEY ,
    name varchar(256) NOT NULL ,
    description text ,
    category varchar(64) NOT NULL ,
    price numeric ,
    language varchar(128) NOT NULL ,
    is_active boolean NOT NULL ,
    creator_id int references persons(id) NOT NULL
);

CREATE TABLE IF NOT EXISTS materials (
    id serial PRIMARY KEY ,
    data text NOT NULL ,
    material_type_id int Not Null ,
    course_id int references courses(id) NOT NULL
);

CREATE TABLE IF NOT EXISTS questions (
    id serial PRIMARY KEY ,
    question text NOT NULL ,
    course_id int references courses(id) NOT NULL
);

CREATE TABLE IF NOT EXISTS answers (
    id serial PRIMARY KEY ,
    answer text NOT NULL ,
    is_correct boolean NOT NULL ,
    question_id int references questions(id) NOT NULL
);

CREATE TABLE IF NOT EXISTS person_learn_course (
  id serial PRIMARY KEY ,
  student_id int references persons(id) NOT NULL ,
  course_id int references courses(id) NOT NULL
);

package com.t3leem_live.models;

import java.io.Serializable;

public class JoinCenterModel implements Serializable {
    private int id;
    private int student_id;
    private int teacher_group_id;
    private int subject_id;
    private int center_group_student_id;
    private String book_type;
    private StudentBookFk student_book_fk;
    private StageClassModel subject_fk;
    private CenterGroupStudentFk center_group_student_fk;


    public int getId() {
        return id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public int getTeacher_group_id() {
        return teacher_group_id;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public int getCenter_group_student_id() {
        return center_group_student_id;
    }

    public String getBook_type() {
        return book_type;
    }

    public StudentBookFk getStudent_book_fk() {
        return student_book_fk;
    }

    public StageClassModel getSubject_fk() {
        return subject_fk;
    }

    public CenterGroupStudentFk getCenter_group_student_fk() {
        return center_group_student_fk;
    }

    public static class StudentBookFk implements Serializable {
        private int id;
        private int teacher_id;
        private int stage_id;
        private int class_id;
        private String department_id;
        private int subject_id;
        private String title;
        private String title_en;
        private String desc;
        private int student_limit;
        private TeacherModel teacher_fk;

        public int getId() {
            return id;
        }

        public int getTeacher_id() {
            return teacher_id;
        }

        public int getStage_id() {
            return stage_id;
        }

        public int getClass_id() {
            return class_id;
        }

        public String getDepartment_id() {
            return department_id;
        }

        public int getSubject_id() {
            return subject_id;
        }

        public String getTitle() {
            return title;
        }

        public String getTitle_en() {
            return title_en;
        }

        public String getDesc() {
            return desc;
        }

        public int getStudent_limit() {
            return student_limit;
        }

        public TeacherModel getTeacher_fk() {
            return teacher_fk;
        }
    }

    public static class CenterGroupStudentFk implements Serializable{
        private int id;
        private int student_id;
        private int center_id;
        private int center_group_id;
        private double center_price;
        private TeacherModel center_fk;

        public int getId() {
            return id;
        }

        public int getStudent_id() {
            return student_id;
        }

        public int getCenter_id() {
            return center_id;
        }

        public int getCenter_group_id() {
            return center_group_id;
        }

        public double getCenter_price() {
            return center_price;
        }

        public TeacherModel getCenter_fk() {
            return center_fk;
        }
    }
}

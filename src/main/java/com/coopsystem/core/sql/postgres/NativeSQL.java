package com.coopsystem.core.sql.postgres;


public class NativeSQL {

    public static final  String FIND_TASK_BY_MANY_PARAMETER =
        "select * from task\n" +
            " where ( isNullText(CAST(:status as TEXT)) \n" +
            "         or life_cycle =CAST(:status as TEXT)) \n" +
            " and   ( isNullText(CAST(:title as TEXT)) \n" +
            "         or title like CAST(:title as TEXT)) \n" +
            " and   ( isNullText(CAST(:env as TEXT)) \n" +
            "         or enviroment like CAST(:env as TEXT)) \n" +
            " and ( isNullText(CAST(:priority as TEXT)) \n" +
            "         or priority like CAST(:priority as TEXT)) \n" +
            " and ( isNullText(CAST(:version as TEXT)) \n" +
        "       or version like CAST(:version as TEXT))  \n" +
            " and ( isNullText(CAST(:projectId as TEXT)) \n" +
            "       or CAST(project_id as TEXT) = coalesce(CAST(:projectId as TEXT),'0'))  \n" +
            " and created_date < :createdDateBefore \n" +
            " and created_date > :createdDateAfter\n" +
            " and modify_date < :modifyDateBefore\n" +
            " and modify_date > :modifyDateAfter\n" +
            " and j_group_id = :jGroupId\n" +
            " and id in(\n" +
            "            select task_id from  (select task_id, PARSE_TXT_BLOB(content) as str  from task_description) subQ \n" +
            "                           where str like :description ) \n" +
            " and id in(  select id from (select s1.id, PARSE_TXT_BLOB(content) as str from \n" +
            "                                   (select task.id,content from task left join comment \n" +
            "                                       on task.id = comment.task_id where task.j_group_id=:jGroupId) as s1) subQ \n" +
            "                               where str like CAST(:comment as TEXT) \n" +
            "                               or isNullText(CAST(:comment as TEXT)))\n";

    public static final String FIND_TASK_BY_MANY_PARAMETER_WITH_ASSIGNEE = FIND_TASK_BY_MANY_PARAMETER+
        "  and assignee_id = :assigneeId\n";
}

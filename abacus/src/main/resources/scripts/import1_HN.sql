create sequence seq_id;
create table bud_detail (id int8 not null, date_created timestamp, date_updated timestamp, user_created varchar(30), user_updated varchar(30), version int4 not null, accrue_date date, budget_amount numeric(12, 2) not null, budget_rx varchar(30) not null, budget_type varchar(30) not null, document_id int8 not null, fiscal_period_id varchar(30) not null, primary key (id));
create table bud_document (id int8 not null, date_created timestamp, date_updated timestamp, user_created varchar(30), user_updated varchar(30), version int4 not null, budget_note varchar(255), fiscal_year_id varchar(30) not null, primary key (id));
create table cat_menu_info (id int8 not null, date_created timestamp, date_updated timestamp, user_created varchar(30), user_updated varchar(30), version int4 not null, count_prepare numeric(10, 0) not null, count_total numeric(10, 0) not null, unit_price numeric(10, 2) not null, fiscal_year_id varchar(30) not null, meal_id int8 not null, primary key (id));
create table cat_menu_item (id int8 not null, date_created timestamp, date_updated timestamp, user_created varchar(30), user_updated varchar(30), version int4 not null, unit_item_count numeric(10, 3) not null, item_id int8 not null, menu_id int8 not null, unit_id int8 not null, primary key (id));
create table cat_menu (id int8 not null, date_created timestamp, date_updated timestamp, user_created varchar(30), user_updated varchar(30), version int4 not null, cancel_reason varchar(255), count_prepare numeric(10, 3) not null, count_sale numeric(10, 3) not null, menu_date date not null, menu_status varchar(30) not null, document_fin_id int8, fiscal_year_id varchar(30) not null, menu_info_id int8 not null, document_stk_id int8, primary key (id));
create table def_item_product (id int8 not null, date_created timestamp, date_updated timestamp, user_created varchar(30), user_updated varchar(30), version int4 not null, material_count numeric(10, 3) not null, material_order numeric(10, 3) not null, item_id int8 not null, material_item_id int8 not null, unit_code_id int8 not null, primary key (id));
create table def_item_unit (id int8 not null, date_created timestamp, date_updated timestamp, user_created varchar(30), user_updated varchar(30), version int4 not null, item_id int8 not null, unit_code_id int8 not null, primary key (id));
create table def_item_value (id int8 not null, date_created timestamp, date_updated timestamp, user_created varchar(30), user_updated varchar(30), version int4 not null, value_type_enum varchar(30) not null, item_id int8 not null, value_id int8 not null, primary key (id));
create table def_item (id int8 not null, date_created timestamp, date_updated timestamp, user_created varchar(30), user_updated varchar(30), version int4 not null, is_active int4 not null, code varchar(255) not null, class_enum varchar(30), name varchar(255) not null, category_id int8 not null, organization_id varchar(30) not null, type_id varchar(30) not null, unit_group_id int8, primary key (id));
create table def_param_answer (id int8 not null, date_created timestamp, date_updated timestamp, user_created varchar(30), user_updated varchar(30), version int4 not null, answer varchar(255) not null, organization_id varchar(30) not null, param_id varchar(30) not null, primary key (id));
create table def_param (id varchar(30) not null, code varchar(30) not null, name varchar(255) not null, type_id varchar(30) not null, primary key (id));
create table def_reference (id int8 not null, date_created timestamp, date_updated timestamp, user_created varchar(30), user_updated varchar(30), version int4 not null, ref_type_id varchar(30) not null, organization_id varchar(30) not null, ref_value_id int8, type_id varchar(30) not null, primary key (id));
create table def_task (id int8 not null, date_created timestamp, date_updated timestamp, user_created varchar(30), user_updated varchar(30), version int4 not null, is_active int4 not null, code varchar(255) not null, item_type_detail varchar(30), item_type_document varchar(30), name varchar(255) not null, organization_id varchar(30) not null, type_id varchar(30) not null, primary key (id));
create table def_type (id varchar(30) not null, level int4 not null, name varchar(255) not null, tr_state_type int4 not null check (tr_state_type<=1 AND tr_state_type>=-1), primary key (id));
create table def_unit_code (id int8 not null, date_created timestamp, date_updated timestamp, user_created varchar(30), user_updated varchar(30), version int4 not null, code varchar(255) not null, name varchar(255) not null, ratio numeric(10, 3) not null, unit_group_id int8 not null, primary key (id));
create table def_unit_group (id int8 not null, date_created timestamp, date_updated timestamp, user_created varchar(30), user_updated varchar(30), version int4 not null, code varchar(255) not null, name varchar(255) not null, organization_id varchar(30) not null, primary key (id));
create table def_value_level (id varchar(30) not null, level_asc int4 not null, level_desc int4 not null, parent_id int8 not null, type_id varchar(30) not null, value_id int8 not null, primary key (id));
create table def_value (id int8 not null, date_created timestamp, date_updated timestamp, user_created varchar(30), user_updated varchar(30), version int4 not null, is_active int4 not null, code varchar(255) not null, name varchar(255) not null, organization_id varchar(30) not null, parent_id int8, type_id varchar(30) not null, primary key (id));
create table fin_document (id int8 not null, date_created timestamp, date_updated timestamp, user_created varchar(30), user_updated varchar(30), version int4 not null, doc_date date not null, doc_no varchar(30) not null, doc_note varchar(255), due_date date, type_id varchar(30) not null, ref_fin_document_id int8, fiscal_period1_id varchar(30) not null, fiscal_period2_id varchar(30) not null, item_id int8, organization_id varchar(30) not null, task_id int8 not null, primary key (id));
create table org_department (id int8 not null, date_created timestamp, date_updated timestamp, user_created varchar(30), user_updated varchar(30), version int4 not null, code varchar(255) not null, group_enum varchar(30) not null, name varchar(255) not null, organization_id varchar(30) not null, primary key (id));
create table org_fiscal_period (id varchar(30) not null, date_finish date, date_start date, is_acc_active int4 not null, is_fin_active int4 not null, is_stk_active int4 not null, period_no varchar(2) not null, fiscal_year_id varchar(30) not null, primary key (id));
create table org_fiscal_year (id varchar(30) not null, date_finish date, date_start date, name varchar(255) not null, customer_id int8, organization_id varchar(30) not null, profit_rate numeric(5, 2) not null,  primary key (id));
create table org_organization (id varchar(30) not null, level_enum varchar(30) not null, name varchar(255) not null, customer_id int8, parent_id varchar(30), primary key (id));
create table rep_pivot (id int8 not null, date_created timestamp, date_updated timestamp, user_created varchar(30), user_updated varchar(30), version int4 not null, field_col_list varchar(500), field_row_list varchar(500), field_val_list varchar(30), name varchar(255) not null, sql_text varchar(2000) not null, organization_id varchar(30) not null, primary key (id));
create table req_detail_offer (id int8 not null, date_created timestamp, date_updated timestamp, user_created varchar(30), user_updated varchar(30), version int4 not null, is_selected int4 not null, offer_note varchar(255), unit_offer_prive numeric(12, 2) not null, detail_id int8 not null, vendor_item_id int8 not null, primary key (id));
create table req_document (id int8 not null, date_created timestamp, date_updated timestamp, user_created varchar(30), user_updated varchar(30), version int4 not null, doc_date date not null, doc_no varchar(30) not null, doc_note varchar(255), due_date date, type_id varchar(30) not null, request_status varchar(30) not null, fiscal_period1_id varchar(30) not null, fiscal_period2_id varchar(30) not null, item_id int8, organization_id varchar(30) not null, task_id int8 not null, department_id int8, department_opp_id int8, primary key (id));
create table sec_authority (id varchar(30) not null, code varchar(30) not null, name varchar(255) not null, primary key (id));
create table sec_group_authority (id int8 not null, date_created timestamp, date_updated timestamp, user_created varchar(30), user_updated varchar(30), version int4 not null, authority_id varchar(30) not null, group_id int8 not null, primary key (id));
create table sec_group (id int8 not null, date_created timestamp, date_updated timestamp, user_created varchar(30), user_updated varchar(30), version int4 not null, name varchar(255) not null, primary key (id));
create table sec_user_department (id int8 not null, date_created timestamp, date_updated timestamp, user_created varchar(30), user_updated varchar(30), version int4 not null, auth_input int4 not null, auth_output int4 not null, department_id int8 not null, user_id varchar(30) not null, primary key (id));
create table sec_user_group (id int8 not null, date_created timestamp, date_updated timestamp, user_created varchar(30), user_updated varchar(30), version int4 not null, group_id int8 not null, user_id varchar(30) not null, primary key (id));
create table sec_user_organization (id int8 not null, date_created timestamp, date_updated timestamp, user_created varchar(30), user_updated varchar(30), version int4 not null, organization_id varchar(30) not null, user_id varchar(30) not null, primary key (id));
create table sec_user (id varchar(30) not null, is_active int4 not null, password varchar(255) not null, organization_root varchar(2), vendor_id int8,primary key (id));
create table stk_detail_track (id int8 not null, date_created timestamp, date_updated timestamp, user_created varchar(30), user_updated varchar(30), version int4 not null, base_track_count numeric(10, 3) not null, base_used_count numeric(10, 3) not null, batch_track_no varchar(30), due_track_date date not null, tr_state_track int4 not null check (tr_state_track<=1 AND tr_state_track>=-1), unit_cost_price numeric(12, 2) not null, unit_track_price numeric(12, 2) not null, detail_id int8 not null, parent_track_id int8, root_detail_id int8 not null, primary key (id));
create table stk_document (id int8 not null, date_created timestamp, date_updated timestamp, user_created varchar(30), user_updated varchar(30), version int4 not null, doc_date date not null, doc_no varchar(30) not null, doc_note varchar(255), due_date date, type_id varchar(30) not null, ref_fin_document_id int8, ref_stk_document_id int8, fiscal_period1_id varchar(30) not null, fiscal_period2_id varchar(30) not null, item_id int8, organization_id varchar(30) not null, task_id int8 not null, primary key (id));
create table tra_detail (id int8 not null, date_created timestamp, date_updated timestamp, user_created varchar(30), user_updated varchar(30), version int4 not null, base_detail_amount numeric(12, 2) not null, base_detail_count numeric(10, 3) not null, det_note varchar(255), due_date date not null, item_detail_count numeric(10, 3) not null, resource varchar(30) not null, tr_state_detail int4 not null check (tr_state_detail<=1 AND tr_state_detail>=-1), unit_detail_price numeric(12, 2) not null, batch_detail_no varchar(30), ref_stk_detail_id int8, glc_id varchar(30), item_id int8 not null, item_unit_id int8, department_id int8, department_opp_id int8, document_req_id int8, document_stk_id int8, bs_document_id int8, document_fin_id int8, primary key (id));
alter table bud_detail add constraint FK_echwoibq98kvtsfi8pya5ayx6 foreign key (document_id) references bud_document;
alter table bud_detail add constraint FK_d06g2dv93ev65ff5p8b69orj foreign key (fiscal_period_id) references org_fiscal_period;
alter table bud_document add constraint FK_cktj0qai65flm4slmo5f1iwsv foreign key (fiscal_year_id) references org_fiscal_year;
alter table cat_menu_info add constraint FK_klxbt69360dgxeq7rvqte44c0 foreign key (fiscal_year_id) references org_fiscal_year;
alter table cat_menu_info add constraint FK_pc840kvexffsnln43fgsc7h81 foreign key (meal_id) references def_item;
alter table cat_menu_item add constraint FK_m2898fraojt6myi8bgfs9ojsl foreign key (item_id) references def_item;
alter table cat_menu_item add constraint FK_mj9nr9wijhlttckxengut6q2e foreign key (menu_id) references cat_menu;
alter table cat_menu_item add constraint FK_6rekvuex3y48kcx4n9ftdbgtm foreign key (unit_id) references def_unit_code;
alter table cat_menu add constraint FK_3pafffob7oh3o253ng09e1l8t foreign key (document_fin_id) references fin_document;
alter table cat_menu add constraint FK_stx7t9jb8buddvckhl3fh02q8 foreign key (fiscal_year_id) references org_fiscal_year;
alter table cat_menu add constraint FK_lj6e477vkbjvh7otutyr6mjt5 foreign key (menu_info_id) references cat_menu_info;
alter table cat_menu add constraint FK_vbe8p1ma4qx4us60b1gf179q foreign key (document_stk_id) references stk_document;
alter table def_item_product add constraint FK_2kffjlfbp4p1iwbaqvm3hnnk3 foreign key (item_id) references def_item;
alter table def_item_product add constraint FK_3dmbocqun6107qca29pt89i2m foreign key (material_item_id) references def_item;
alter table def_item_product add constraint FK_j96hneeamsecwpgo2lfr71it9 foreign key (unit_code_id) references def_unit_code;
alter table def_item_unit add constraint FK_3i676lfdp9vawhr37n6qistvo foreign key (item_id) references def_item;
alter table def_item_unit add constraint FK_jipp9fnj0ilus1ppf2c8m6pm0 foreign key (unit_code_id) references def_unit_code;
alter table def_item_value add constraint FK_6vlcfipmajs64biqc37j38brw foreign key (item_id) references def_item;
alter table def_item_value add constraint FK_oidskh0g4ni0si8t2s6p29vg8 foreign key (value_id) references def_value;
alter table def_item add constraint FK_pleuc3hb4kupcaf17n046xeek foreign key (category_id) references def_value;
alter table def_item add constraint FK_2sp8w96ivig5ll4hs11anhyok foreign key (organization_id) references org_organization;
alter table def_item add constraint FK_or6y6lsrfi39qwx93mujdf29i foreign key (type_id) references def_type;
alter table def_item add constraint FK_emmp0t58ryneduc49j2pr67to foreign key (unit_group_id) references def_unit_group;
alter table def_param_answer add constraint FK_k4dkn8rn73hdbcoc29tt9hksi foreign key (organization_id) references org_organization;
alter table def_param_answer add constraint FK_2uv7tpqsyydgfkfdrnr0fgmkq foreign key (param_id) references def_param;
alter table def_param add constraint FK_o98sdh4kq0nnyv6dujpelte1x foreign key (type_id) references def_type;
alter table def_reference add constraint FK_qdiy4t4hvs81o890od6vrbo4 foreign key (organization_id) references org_organization;
alter table def_reference add constraint FK_i03hyopbsyi4g0tirmma0nslb foreign key (ref_value_id) references def_value;
alter table def_reference add constraint FK_ceao7x5y7p3rh3iic65e0312q foreign key (type_id) references def_type;
alter table def_task add constraint FK_33jd0rkj4a3wujwaj6h8m79dk foreign key (organization_id) references org_organization;
alter table def_task add constraint FK_ps7fc5g069ctufbplx6ujqo66 foreign key (type_id) references def_type;
alter table def_unit_code add constraint FK_m7lvd455blrea19y4siucjtid foreign key (unit_group_id) references def_unit_group;
alter table def_unit_group add constraint FK_57ti6d5oayf95j16qu2i2p3lp foreign key (organization_id) references org_organization;
alter table def_value_level add constraint FK_3t9slv762vo3ld4j99uke1tyl foreign key (parent_id) references def_value;
alter table def_value_level add constraint FK_dvrwl2fryoo8msrhnjjwpqdlm foreign key (type_id) references def_type;
alter table def_value_level add constraint FK_1ujvmr5yft5f01tvn14hbddpw foreign key (value_id) references def_value;
alter table def_value add constraint FK_32om8q4mlrhehyvuphdw9yw0j foreign key (organization_id) references org_organization;
alter table def_value add constraint FK_l021ifgrex10trni8uitdijw7 foreign key (parent_id) references def_value;
alter table def_value add constraint FK_ir3lxcxfuf5ohytb4m7fvfgwl foreign key (type_id) references def_type;
alter table fin_document add constraint FK_lv9lyoq0apn9286gq6par2qdf foreign key (fiscal_period1_id) references org_fiscal_period;
alter table fin_document add constraint FK_239sk57sqhjcy45xv5akyxibd foreign key (fiscal_period2_id) references org_fiscal_period;
alter table fin_document add constraint FK_jlx2sgt8s9e3jqm4c9bayhw5o foreign key (item_id) references def_item;
alter table fin_document add constraint FK_38pwd7k8jo7ng719m4hk2r9ho foreign key (organization_id) references org_organization;
alter table fin_document add constraint FK_juay7hqgwl290ab49ynlbt5ge foreign key (task_id) references def_task;
alter table org_department add constraint FK_e6euduu90vf71dkk7fv2i9d7d foreign key (organization_id) references org_organization;
alter table org_fiscal_period add constraint FK_maqybm7kl5soebdi0q1sdpo7d foreign key (fiscal_year_id) references org_fiscal_year;
alter table org_fiscal_year add constraint FK_s5kac5du5xk1brep8or574s76 foreign key (customer_id) references def_item;
alter table org_fiscal_year add constraint FK_13wxkqf31x31ppmg5977x05b8 foreign key (organization_id) references org_organization;
alter table org_organization add constraint FK_rbra3v04cvx2cu6c0r6n6mkqj foreign key (customer_id) references def_item;
alter table org_organization add constraint FK_4dx6ixp0467bd5oh5u6qiel6v foreign key (parent_id) references org_organization;
alter table rep_pivot add constraint FK_hloj8xrkphkaucfk5qdscisap foreign key (organization_id) references org_organization;
alter table req_detail_offer add constraint FK_mx79idbmcbr1jfvpfh64mbw7j foreign key (detail_id) references tra_detail;
alter table req_detail_offer add constraint FK_6nou98xn2wtv2ouppkw3j7g6h foreign key (vendor_item_id) references def_item;
alter table req_document add constraint FK_so7nu8ane1g3mtqcnv2v6jfvp foreign key (fiscal_period1_id) references org_fiscal_period;
alter table req_document add constraint FK_1mgor84e9ftwar12snxj5huin foreign key (fiscal_period2_id) references org_fiscal_period;
alter table req_document add constraint FK_qvip5miyj2pjkgn673a1mlwcv foreign key (item_id) references def_item;
alter table req_document add constraint FK_c8tnnqnqs4m0gk7tn5rk2embn foreign key (organization_id) references org_organization;
alter table req_document add constraint FK_67pm0rxqncfnhijdm1oskipil foreign key (task_id) references def_task;
alter table req_document add constraint FK_belvt57q7c3x694003ud10rx0 foreign key (department_id) references org_department;
alter table req_document add constraint FK_9o11u5g8hqc31e6gbac4m2ilo foreign key (department_opp_id) references org_department;
alter table sec_group_authority add constraint FK_2q1p0q8dvd26sipsn4hmynh8p foreign key (authority_id) references sec_authority;
alter table sec_group_authority add constraint FK_qx9wplbx5b97d552n9mxw4fvn foreign key (group_id) references sec_group;
alter table sec_user_department add constraint FK_llxow8dms4j6cdl6k90n8lnuq foreign key (department_id) references org_department;
alter table sec_user_department add constraint FK_doqru8ey1kpmgwax5det0fgm0 foreign key (user_id) references sec_user;
alter table sec_user_group add constraint FK_cddpti40avr3rv05yq4iqpdbu foreign key (group_id) references sec_group;
alter table sec_user_group add constraint FK_d3n84hamdsatkgnc5vf6qg1hl foreign key (user_id) references sec_user;
alter table sec_user_organization add constraint FK_lkr7jns3gv096i9ratjxt3qnl foreign key (organization_id) references org_organization;
alter table sec_user_organization add constraint FK_e4x3rnb1oyiuihhsv04e7vs5n foreign key (user_id) references sec_user;
alter table sec_user add constraint FK_4docurlh3y3syttkv950visi5 foreign key (vendor_id) references def_item;
alter table stk_detail_track add constraint FK_9eienpnstr01hv470utbwbvrs foreign key (detail_id) references tra_detail;
alter table stk_detail_track add constraint FK_t018k6kryql7ydxi136jj77tp foreign key (parent_track_id) references stk_detail_track;
alter table stk_detail_track add constraint FK_91s2j76ltg1g4hm2nufcgb9dy foreign key (root_detail_id) references tra_detail;
alter table stk_document add constraint FK_emflrnti08f5kn8047llhem51 foreign key (fiscal_period1_id) references org_fiscal_period;
alter table stk_document add constraint FK_6b9jolxi3vl4vxbep9joqh4bf foreign key (fiscal_period2_id) references org_fiscal_period;
alter table stk_document add constraint FK_jk8drjldxvv8vpoesy2m54u5h foreign key (item_id) references def_item;
alter table stk_document add constraint FK_j3ljb0l5m0iqds0fk9jta5wwp foreign key (organization_id) references org_organization;
alter table stk_document add constraint FK_rpu1ifk4n8mt1prhyiu9aciup foreign key (task_id) references def_task;
alter table tra_detail add constraint FK_2igi44duq18rt7xjls37uqx7u foreign key (item_id) references def_item;
alter table tra_detail add constraint FK_q8m57lsj5fevwlj19s2qa37ms foreign key (item_unit_id) references def_unit_code;
alter table tra_detail add constraint FK_e8aceyw4dn69fped6c1x4wc0a foreign key (department_id) references org_department;
alter table tra_detail add constraint FK_5s8abk6ucw06h2wifl56jf27p foreign key (department_opp_id) references org_department;
alter table tra_detail add constraint FK_3eakj9iic4r6v5t7syw2uuwl4 foreign key (document_req_id) references req_document;
alter table tra_detail add constraint FK_r3kh35m70fntv1anhnj6j1y89 foreign key (document_stk_id) references stk_document;
alter table tra_detail add constraint FK_r0xmdtii05bl5dc00wfspstac foreign key (bs_document_id) references fin_document;
alter table tra_detail add constraint FK_jl38bho6qtncv0adr41f929tx foreign key (document_fin_id) references fin_document;

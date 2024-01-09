CREATE USER wapi_root WITH PASSWORD 'password';
grant all privileges on database waifudb to wapi_root;
GRANT pg_read_all_data TO wapi_root;
GRANT pg_write_all_data TO wapi_root;
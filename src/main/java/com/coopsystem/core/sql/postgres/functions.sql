
CREATE OR REPLACE FUNCTION isNullText(msg text)
    RETURNS boolean AS
$$
DECLARE
    result boolean;
BEGIN
    result := false;
    IF coalesce(CAST(msg as TEXT), 'a'||coalesce(msg,'abc')) = 'a'||coalesce(msg,'abc') THEN
       result:=true;
    END IF;
    RETURN result;
END;
$$
LANGUAGE 'plpgsql' IMMUTABLE
SECURITY DEFINER
  COST 10;
COMMIT;

CREATE OR REPLACE FUNCTION PARSE_TXT_BLOB(content text)
    RETURNS text AS
$$
DECLARE
    result text;
BEGIN
    result := convert_from( loread( lo_open(cast(content as int), cast(x'40000' as int)), cast(x'40000' as int)),  'UTF8');
    RETURN result;
END;
$$
LANGUAGE 'plpgsql' IMMUTABLE
SECURITY DEFINER
  COST 10;
COMMIT;

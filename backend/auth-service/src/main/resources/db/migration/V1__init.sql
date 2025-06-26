CREATE OR REPLACE FUNCTION uuidv7(P_TIMESTAMP TIMESTAMP WITH TIME ZONE DEFAULT CLOCK_TIMESTAMP())
    RETURNS UUID
    LANGUAGE PLPGSQL
AS
$$
DECLARE
    v_time       DOUBLE PRECISION;
    v_unix_t     BIGINT;
    v_rand_a     BIGINT;
    v_rand_b     BIGINT;
    v_unix_t_hex VARCHAR;
    v_rand_a_hex VARCHAR;
    v_rand_b_hex VARCHAR;
    c_milli      DOUBLE PRECISION := 1_000;
    c_micro      DOUBLE PRECISION := 1_000_000;
    c_scale      DOUBLE PRECISION := 4.096; -- 4.0 * (1024 / 1000)

    c_version    BIGINT           := X'0000000000007000'::BIGINT; -- RFC 9562 version: 0111
    c_variant    BIGINT           := X'8000000000000000'::BIGINT; -- RFC 9562 variant: 10xx
BEGIN
    v_time := EXTRACT(EPOCH FROM P_TIMESTAMP);

    v_unix_t := TRUNC(v_time * c_milli);
    v_rand_a := TRUNC((v_time * c_micro - v_unix_t * c_milli) * c_scale);
    v_rand_b := (TRUNC(RANDOM() * 2 ^ 30)::BIGINT << 32) | TRUNC(RANDOM() * 2 ^ 32)::BIGINT;

    v_unix_t_hex := LPAD(TO_HEX(v_unix_t), 12, '0');
    v_rand_a_hex := LPAD(TO_HEX((v_rand_a | c_version)::BIGINT), 4, '0');
    v_rand_b_hex := LPAD(TO_HEX((v_rand_b | c_variant)::BIGINT), 16, '0');

    RETURN (v_unix_t_hex || v_rand_a_hex || v_rand_b_hex)::UUID;
END;
$$;

CREATE TABLE users
(
    id       UUID DEFAULT uuidv7() PRIMARY KEY,
    email    VARCHAR(254) UNIQUE NOT NULL,
    password VARCHAR(72)         NOT NULL
);

-- Database: bancoapp

-- DROP DATABASE IF EXISTS bancoapp;

CREATE DATABASE bancoapp
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Spanish_Ecuador.1252'
    LC_CTYPE = 'Spanish_Ecuador.1252'
    LOCALE_PROVIDER = 'libc'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;


--------------TABLA CLIENTES-----------------------------------------

-- Table: public.clientes

-- DROP TABLE IF EXISTS public.clientes;

CREATE TABLE IF NOT EXISTS public.clientes
(
    id bigint NOT NULL DEFAULT nextval('clientes_id_seq'::regclass),
    direccion character varying(255) COLLATE pg_catalog."default",
    edad integer NOT NULL,
    genero character varying(255) COLLATE pg_catalog."default",
    identificacion character varying(255) COLLATE pg_catalog."default",
    nombre character varying(255) COLLATE pg_catalog."default",
    telefono character varying(255) COLLATE pg_catalog."default",
    contrasena character varying(255) COLLATE pg_catalog."default" NOT NULL,
    estado boolean NOT NULL,
    CONSTRAINT clientes_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.clientes
    OWNER to postgres;

--------------TABLA CUENTAS-----------------------------------------
-- Table: public.cuentas

-- DROP TABLE IF EXISTS public.cuentas;

CREATE TABLE IF NOT EXISTS public.cuentas
(
    id bigint NOT NULL DEFAULT nextval('cuentas_id_seq'::regclass),
    estado boolean NOT NULL,
    numero_cuenta character varying(255) COLLATE pg_catalog."default" NOT NULL,
    saldo_inicial numeric(38,2) NOT NULL,
    tipo_cuenta character varying(255) COLLATE pg_catalog."default" NOT NULL,
    cliente character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT cuentas_pkey PRIMARY KEY (id),
    CONSTRAINT uk_7h7mqvcau3mcl0mbrkdrt7fnh UNIQUE (numero_cuenta)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.cuentas
    OWNER to postgres;
--------------TABLA MOVIMIENTOS-----------------------------------------

    -- Table: public.movimientos

-- DROP TABLE IF EXISTS public.movimientos;

CREATE TABLE IF NOT EXISTS public.movimientos
(
    id bigint NOT NULL DEFAULT nextval('movimientos_id_seq'::regclass),
    fecha date NOT NULL,
    numero_cuenta character varying(255) COLLATE pg_catalog."default" NOT NULL,
    saldo numeric(38,2) NOT NULL,
    tipo_movimiento character varying(255) COLLATE pg_catalog."default" NOT NULL,
    valor numeric(38,2) NOT NULL,
    CONSTRAINT movimientos_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.movimientos
    OWNER to postgres;


-- Table: public.transacciones

-- DROP TABLE IF EXISTS public.transacciones;

CREATE TABLE IF NOT EXISTS public.transacciones
(
    id bigint NOT NULL DEFAULT nextval('transacciones_id_seq'::regclass),
    fecha timestamp(6) without time zone NOT NULL,
    ip_origen character varying(255) COLLATE pg_catalog."default" NOT NULL,
    numero_cuenta character varying(255) COLLATE pg_catalog."default" NOT NULL,
    request_json text COLLATE pg_catalog."default",
    response_json text COLLATE pg_catalog."default",
    saldo_disponible numeric(38,2) NOT NULL,
    saldo_inicial numeric(38,2) NOT NULL,
    tipo_movimiento character varying(255) COLLATE pg_catalog."default" NOT NULL,
    valor numeric(38,2) NOT NULL,
    CONSTRAINT transacciones_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.transacciones
    OWNER to postgres;
--------------FUNCION GENERA REPORTE ESTADO DE CUENTA-----------------------------------------
CREATE OR REPLACE FUNCTION public.fn_movimientos_cuenta(
	p_cliente character varying,
	p_fecha_inicio date DEFAULT NULL::date,
	p_fecha_fin date DEFAULT NULL::date)
    RETURNS TABLE(fecha text, cliente character varying, numerocuenta character varying, tipo character varying, saldoinicial numeric, estado boolean, movimiento numeric, saldodisponible numeric) 
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$
BEGIN
    RETURN QUERY
  SELECT TO_CHAR(m.fecha::DATE, 'DD/MM/YYYY') as fecha, 
       c.cliente, 
       c.numero_cuenta AS numeroCuenta, 
       c.tipo_cuenta AS tipo, 
        CASE 
           WHEN m.tipo_movimiento = 'Retiro' THEN c.saldo_inicial + m.valor
           WHEN m.tipo_movimiento = 'Deposito' THEN c.saldo_inicial - m.valor
           ELSE c.saldo_inicial
       END AS saldoInicial,
       c.estado,

	 CASE 
           WHEN m.tipo_movimiento = 'Retiro' THEN c.saldo_inicial - m.valor
		   WHEN m.tipo_movimiento = 'Deposito' THEN m.valor
           ELSE c.saldo_inicial
       END AS movimiento,
       c.saldo_inicial AS saldoDisponible
    FROM public.cuentas c 
    INNER JOIN public.movimientos m ON m.numero_cuenta = c.numero_cuenta
    WHERE (p_cliente IS NULL OR c.cliente = p_cliente)
    AND (p_fecha_inicio IS NULL OR m.fecha >= p_fecha_inicio)
    AND (p_fecha_fin IS NULL OR m.fecha <= p_fecha_fin);
END;
$$ LANGUAGE plpgsql;

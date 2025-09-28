create table fonds(
    id varchar(64) primary key,
    intitule text not null
);

create table dossier(
    id varchar(64) primary key,
    intitule text not null,
    cote varchar(64) not null,
    fonds_id varchar(64) references fonds(id),
    unique(fonds_id, cote)
);

create table piece(
    id varchar(64) primary key,
    intitule text not null,
    dossier_id varchar(64) not null references dossier(id)
);
-- Seed data for Movie Service (H2 local dev only)
-- Spring runs this automatically on startup.
-- Skipped in Docker because spring.sql.init.mode=embedded
INSERT INTO movies (title, genre, release_year) VALUES ('The Godfather', 'Crime', 1972);
INSERT INTO movies (title, genre, release_year) VALUES ('Inception', 'Sci-Fi', 2010);
INSERT INTO movies (title, genre, release_year) VALUES ('Pulp Fiction', 'Crime', 1994);
INSERT INTO movies (title, genre, release_year) VALUES ('The Dark Knight', 'Action', 2008);
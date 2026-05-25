-- Create VOTERS table
CREATE TABLE voters (
    voter_id NUMBER PRIMARY KEY,
    full_name VARCHAR2(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    address VARCHAR2(200),
    city VARCHAR2(50),
    password VARCHAR2(50) NOT NULL,
    has_voted VARCHAR2(3) DEFAULT 'NO',
    registration_date DATE DEFAULT SYSDATE
);

-- Create PARTIES table
CREATE TABLE parties (
    party_id NUMBER PRIMARY KEY,
    party_name VARCHAR2(50) NOT NULL,
    symbol VARCHAR2(50)
);

-- Create VOTES table
CREATE TABLE votes (
    vote_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    voter_id NUMBER REFERENCES voters(voter_id),
    party_id NUMBER REFERENCES parties(party_id),
    vote_date DATE DEFAULT SYSDATE
);

-- Insert party data
INSERT INTO parties VALUES (1, 'Party A - Lotus', 'Lotus');
INSERT INTO parties VALUES (2, 'Party B - Elephant', 'Elephant');
INSERT INTO parties VALUES (3, 'Party C - Hand', 'Hand');
INSERT INTO parties VALUES (4, 'NOTA', 'None');

-- Create sequence for voter_id
CREATE SEQUENCE voter_seq START WITH 1001 INCREMENT BY 1;

-- Commit changes
COMMIT;

import React from 'react';

import styled from 'styled-components';

import {Navbar, Typography, ContainedButton} from '@diekeditora/ui';

export const Header: React.VFC = () => {
  return (
    <Container>
      <Navbar />

      <Main>
        <Competition>
          <Typography variant="h1" lineHeight="4.875rem" fontSize="4.25rem" color="#FFF">
            Participe do concurso
          </Typography>

          <ParticipateButton size="small" border="large">
            Participar agora
          </ParticipateButton>
        </Competition>
      </Main>

      <Slideshow />
    </Container>
  );
};

const Container = styled.header`
  height: 40.625rem;
  position: relative;

  display: flex;
  flex-direction: column;
`;

const Main = styled.main`
  width: 100%;
  max-width: 68.75rem;
  margin: auto;

  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
`;

const Competition = styled.div`
  max-width: 27.5rem;

  display: flex;
  flex-direction: column;
  gap: 5rem;

  text-shadow: 0px 5px 5px rgba(0, 0, 0, 0.25);
`;

const ParticipateButton = styled(ContainedButton)`
  padding: 15px 0.875rem;
  border-radius: 1.2rem;
  font-size: 14px;
  filter: drop-shadow(0px 5px 5px rgba(0, 0, 0, 0.25));

  width: fit-content;
`;

const Slideshow = styled.div`
  position: absolute;

  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  z-index: -1;

  background: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url('/images/Slideshow1.png');
  background-repeat: no-repeat;
  background-size: cover;
`;

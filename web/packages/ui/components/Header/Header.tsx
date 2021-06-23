import React from 'react';

import Link from 'next/link';

import styled from 'styled-components';

import {FullLogo} from '@diekeditora/ui/icons';
import {Typography, TextButton, ContainedButton} from '@diekeditora/ui';

type NavItem = {
  link: string;
  text: string;
};

const items: NavItem[] = [
  {text: 'Inicio', link: '/'},
  {text: 'Mangas', link: '/mangas'},
  {text: 'Quem somos', link: '/about'},
  {text: 'Suporte', link: '/support'},
];

export const Header: React.VFC = () => {
  return (
    <Container>
      <Navbar>
        <div className="background">
          <div className="blur" />
        </div>

        <Content>
          <FullLogo width="230px" height="75px" />

          <div>
            <List>
              {items.map(({text, link}) => (
                <li key={link}>
                  <Link href="/" passHref>
                    <TextButton as={NavLink} color="neutral" outline={false}>
                      <Typography variant="h4" as="span">
                        {text}
                      </Typography>
                    </TextButton>
                  </Link>
                </li>
              ))}
            </List>
          </div>

          <div>
            <span>
              <input placeholder="Buscar" />
            </span>

            <span>Notificacoes</span>
            <span>Perfil</span>
          </div>
        </Content>
      </Navbar>

      <Main>
        <Competition>
          <Typography variant="h1" lineHeight="4.875rem" fontSize="4.25rem" color="#fff">
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

const Navbar = styled.nav`
  display: flex;
  width: 100%;

  position: relative;

  .background {
    position: absolute;
    left: 0;
    right: 0;
    bottom: 0;
    top: 0;
    content: ' ';
    width: 100%;
    height: 100%;
    z-index: 1;
  }

  .blur {
    background: rgba(255, 255, 255, 0.05);
    backdrop-filter: blur(4px);
    width: 100%;
    height: 100%;
  }
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

const Content = styled.div`
  width: 100%;
  max-width: 87.5rem;
  margin: auto;
  z-index: 2;

  display: flex;
  align-items: center;
  justify-content: space-between;

  div {
    height: 4.68rem;
  }
`;

const List = styled.ul`
  display: flex;
  gap: 1.5rem;
  height: 100%;

  li {
    height: 100%;
  }
`;

const NavLink = styled.a`
  color: #fff;

  height: 100%;
  padding: 0 1.125rem;

  display: flex;
  align-items: center;
`;

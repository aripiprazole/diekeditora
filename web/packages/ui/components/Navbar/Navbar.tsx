import React from 'react';

import Link from 'next/link';

import styled from 'styled-components';

import {FullLogo} from '@diekeditora/ui/icons';
import {Typography, TextButton} from '@diekeditora/ui';

type NavItem = {
  link: string;
  text: string;
};

const items: NavItem[] = [
  {text: 'Início', link: '/'},
  {text: 'Mangás', link: '/mangas'},
  {text: 'Quem somos', link: '/about'},
  {text: 'Suporte', link: '/support'},
];

export const Navbar: React.VFC = () => {
  return (
    <Container>
      <div className="background">
        <div className="blur" />
      </div>

      <Content>
        <FullLogo width="230px" height="75px" />

        <div>
          <List>
            {items.map(({text, link}) => (
              <li key={link}>
                <Link href={link} passHref>
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
    </Container>
  );
};

const Container = styled.nav`
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
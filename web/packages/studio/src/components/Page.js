import PropTypes from 'prop-types';
import {Helmet} from 'react-helmet-async';
import {forwardRef} from 'react';
// material
import {Box} from '@material-ui/core';

// ----------------------------------------------------------------------

function Page({children, title = '', ...other}, ref) {
  return (
    <Box ref={ref} {...other}>
      <Helmet>
        <title>{title}</title>
      </Helmet>
      {children}
    </Box>
  );
}

Page.propTypes = {
  children: PropTypes.node.isRequired,
  title: PropTypes.string,
};

export default forwardRef(Page);

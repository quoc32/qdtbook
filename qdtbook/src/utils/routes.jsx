// routes.jsx
import Home from "../pages/Home";
import Friends from "../pages/Friends";
import Video from "../pages/Video";
import Market from "../pages/Market";
import Game from "../pages/Game";

const routes = [
  { path: "/", element: <Home /> },
  { path: "/home", element: <Home /> },
  { path: "/friends", element: <Friends /> },
  { path: "/video", element: <Video /> },
  { path: "/market", element: <Market /> },
  { path: "/game", element: <Game /> },
];

export default routes;

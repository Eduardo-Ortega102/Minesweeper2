package Application;

import Control.Command;
import Control.ImagePriority;
import Control.ImageViewerControl;
import Control.ImageViewerControlFactory;
import Control.ObserverImageControl;
import Model.Board;
import Model.Cell;
import Model.Clock;
import Model.ImageSet;
import Model.abstractInterface.Bitmap;
import Model.abstractInterface.BoardException;
import Persistence.FileImageLoader;
import Persistence.FileImageSetLoader;
import Persistence.ProxyImage;
import Persistence.abstractInterface.BitmapFactory;
import Persistence.abstractInterface.ImageSetLoader;
import UserInterface.ApplicationFrameSwing;
import UserInterface.BitmapSwing;
import UserInterface.BoardViewerSwing;
import UserInterface.CellViewerSwing;
import UserInterface.ErrorDialogSwing;
import UserInterface.GameOverDialogSwing;
import UserInterface.HelpDialogSwing;
import UserInterface.InfoPanelSwing;
import UserInterface.OptionDialogSwing;
import UserInterface.PanelImageViewer;
import UserInterface.WinnerDialogSwing;
import UserInterface.abstractInterface.Action;
import UserInterface.abstractInterface.ActionFactory;
import UserInterface.abstractInterface.ApplicationFrame;
import UserInterface.abstractInterface.BoardViewer;
import UserInterface.abstractInterface.CellViewer;
import UserInterface.abstractInterface.CellViewerFactory;
import UserInterface.abstractInterface.Dialog;
import UserInterface.abstractInterface.ImageViewer;
import UserInterface.abstractInterface.InfoPanel;
import UserInterface.abstractInterface.OptionDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;

public class Application {

    public static void main(String[] args) {
        new Application().execute();
    }
    private HashMap<String, Command> commandMap;
    private ActionFactory actionListenerFactory;
    private ActionFactory actionFactory;
    private OptionDialog optionDialog;
    private Board board;
    private ApplicationFrame applicationFrame;
    private CellViewerFactory cellViewerFactory;
    private Dialog youWinDialog;
    private Dialog gameOverDialog;
    private Clock clock;
    private ImageViewer imageViewer;
    private ImageViewerControl imageViewerControl;
    private ImageSetLoader imageSetLoader;
    private ImageViewerControlFactory imageControlFactory;

    private void execute() {
        this.actionListenerFactory = createActionListenerFactory();
        this.actionFactory = createActionFactory();
        this.cellViewerFactory = createCellViewerFactory();
        this.optionDialog = createOptionDialog(actionListenerFactory);
        this.imageViewer = createImageViewer();
        this.imageSetLoader = createImageSetLoader();
        this.imageControlFactory = createImageControlFactory();
        this.imageViewerControl = imageControlFactory.createImageViewerControl(this.imageViewer, this.imageSetLoader.loadImageSet());
        this.clock = new Clock();
        this.board = new Board(this.clock);

        this.applicationFrame = new ApplicationFrameSwing(actionListenerFactory, createBoardViewer(),
                createInfoPanel());
        this.createCommands();
        this.optionDialog.showDialog();
    }

    private ActionFactory createActionListenerFactory() {
        return new ActionFactory<ActionListener>() {
            @Override
            public ActionListener createAction(final String action) {
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Command command = commandMap.get(action);
                        if (command != null) command.executeCommand();
                    }
                };
            }
        };
    }

    private ActionFactory createActionFactory() {
        return new ActionFactory<Action>() {
            @Override
            public Action createAction(final String action) {
                return new Action() {
                    @Override
                    public void execute() {
                        Command command = commandMap.get(action);
                        if (command != null) command.executeCommand();
                    }
                };
            }
        };
    }

    private CellViewerFactory createCellViewerFactory() {
        return new CellViewerFactory() {
            @Override
            public CellViewer createCellViewer(Cell cell) {
                CellViewerSwing viewer = new CellViewerSwing(cell, actionFactory);
                ObserverImageControl control = new ObserverImageControl(viewer, createImageSet("src\\Icons"));
                viewer.addObserver(control);
                return viewer;
            }

            private ImageSet createImageSet(String path) {
                ImageSet set = new ImageSet();
                set.put("explodedIcon.jpg", new ProxyImage(
                        new FileImageLoader(path + "\\" + "explodedIcon.jpg", createBitmapFactory())));
                set.put("mineIcon.jpg", new ProxyImage(
                        new FileImageLoader(path + "\\" + "mineIcon.jpg", createBitmapFactory())));
                set.put("markedIcon.jpg", new ProxyImage(
                        new FileImageLoader(path + "\\" + "markedIcon.jpg", createBitmapFactory())));
                set.put("errorMarkedIcon.jpg", new ProxyImage(
                        new FileImageLoader(path + "\\" + "errorMarkedIcon.jpg", createBitmapFactory())));
                return set;
            }
        };
    }

    private OptionDialog createOptionDialog(ActionFactory factory) {
        return new OptionDialogSwing(factory);
    }

    private BoardViewer createBoardViewer() {
        return new BoardViewerSwing(cellViewerFactory, actionFactory);
    }

    private InfoPanel createInfoPanel() {
        return new InfoPanelSwing(board, this.imageViewer);
    }

    private ImageViewer createImageViewer() {
        return new PanelImageViewer();
    }

    private ImageSetLoader createImageSetLoader() {
        return new FileImageSetLoader(
                createBitmapFactory(),
                "src\\Icons",
                "winnerIcon.jpg", "waitIcon.jpg", "moveIcon.jpg", "loserIcon.jpg");
    }

    private BitmapFactory createBitmapFactory() {
        return new BitmapFactory<String>() {
            @Override
            public Bitmap createBitmap(String input) {
                try {
                    return new BitmapSwing(input);
                } catch (IOException ex) {
                    System.out.println("BitmapSwing Error.");
                }
                return null;
            }
        };
    }

    private ImageViewerControlFactory createImageControlFactory() {
        return new ImageViewerControlFactory() {
            @Override
            public ImageViewerControl createImageViewerControl(ImageViewer viewer, ImageSet set) {
                return new ImageViewerControl(imageViewer, set, createHashMap());
            }

            private HashMap<String, ImagePriority> createHashMap() {
                final HashMap<String, ImagePriority> priority = new HashMap<>();
                priority.put("winnerIcon.jpg", ImagePriority.HIGH);
                priority.put("loserIcon.jpg", ImagePriority.HIGH);
                priority.put("waitIcon.jpg", ImagePriority.LOW);
                priority.put("moveIcon.jpg", ImagePriority.LOW);
                return priority;
            }
        };
    }

    private void createCommands() {
        commandMap = new HashMap<>();
        commandMap.put("StartGame", new Command() {
            @Override
            public void executeCommand() {
                optionDialog.hidDialog();
                imageViewerControl.reset();
                runApplication();
            }
        });

        commandMap.put("PlayAgain", new Command() {
            @Override
            public void executeCommand() {
                youWinDialog.hidDialog();
                imageViewerControl.reset();
                runApplication();
            }
        });

        commandMap.put("Options", new Command() {
            @Override
            public void executeCommand() {
                optionDialog.showDialog();
            }
        });

        commandMap.put("NewGame", new Command() {
            @Override
            public void executeCommand() {
                if (gameOverDialog != null) gameOverDialog.hidDialog();
                imageViewerControl.reset();
                runApplication();
            }
        });

        commandMap.put("Help", new Command() {
            @Override
            public void executeCommand() {
                Dialog help = createHelpDialog();
                help.showDialog();
            }
        });

        commandMap.put("Restart", new Command() {
            @Override
            public void executeCommand() {
                gameOverDialog.hidDialog();
                imageViewerControl.reset();
                imageViewerControl.viewImage("waitIcon.jpg");
                applicationFrame.getBoardViewer().restart();
            }
        });

        commandMap.put("YouWin", new Command() {
            @Override
            public void executeCommand() {
                if (youWinDialog == null) youWinDialog = createWinnerDialog();
                imageViewerControl.viewImage("winnerIcon.jpg");
                youWinDialog.showDialog();
            }
        });

        commandMap.put("GameOver", new Command() {
            @Override
            public void executeCommand() {
                if (gameOverDialog == null) gameOverDialog = createOverDialog();
                imageViewerControl.viewImage("loserIcon.jpg");
                gameOverDialog.showDialog();
            }
        });

        commandMap.put("CellViewerSelected", new Command() {
            @Override
            public void executeCommand() {
                imageViewerControl.viewImage("moveIcon.jpg");
            }
        });

        commandMap.put("CellViewerUnselected", new Command() {
            @Override
            public void executeCommand() {
                imageViewerControl.viewImage("waitIcon.jpg");
            }
        });

        commandMap.put("Exit", new Command() {
            @Override
            public void executeCommand() {
                System.exit(0);
            }
        });
    }

    private void runApplication() {
        this.imageViewerControl.viewImage("waitIcon.jpg");
        try {
            board.buildBoard(optionDialog.getRowsAmount(),
                    optionDialog.getColumnAmount(),
                    optionDialog.getMinesAmount());
            applicationFrame.getBoardViewer().load(board);
            applicationFrame.execute();
        } catch (BoardException ex) {
            Dialog errorDialog = createErrorDialog(ex.getMessage());
            errorDialog.showDialog();
            optionDialog.showDialog();
        }
    }

    private Dialog createWinnerDialog() {
        return new WinnerDialogSwing(actionListenerFactory);
    }

    private Dialog createOverDialog() {
        return new GameOverDialogSwing(actionListenerFactory);
    }

    private Dialog createHelpDialog() {
        return new HelpDialogSwing();
    }

    private Dialog createErrorDialog(String message) {
        return new ErrorDialogSwing(message);
    }
}

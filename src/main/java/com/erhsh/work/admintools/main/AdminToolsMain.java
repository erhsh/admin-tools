package com.erhsh.work.admintools.main;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.erhsh.work.admintools.service.IUserService;
import com.erhsh.work.admintools.service.impl.UserServiceImpl;
import com.erhsh.work.admintools.vo.DeviceVO;
import com.erhsh.work.admintools.vo.UserVO;

public class AdminToolsMain {
	private class TableLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			String colText = "";

			UserVO userVO = (UserVO) element;

			switch (columnIndex) {
			case 0:
				colText = userVO.getId();
				break;
			case 1:
				colText = userVO.getLoginName();
				break;
			case 2:
				colText = userVO.getNick();
				break;
			case 3:
				colText = userVO.getFailedLoginTimes();
				break;
			case 4:
				List<DeviceVO> devices = userVO.getDevices();
				if (null == devices) {
					break;
				}
				for (DeviceVO device : devices) {
					colText = colText + "[" + device.getId() + ":"
							+ device.getMac() + "]";
				}
				break;
			case 5:
				colText = userVO.getEmailAvailable();
				break;
			default:
				break;
			}

			return colText;
		}
	}

	private static class ContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List<?>) {
				return ((List<?>) inputElement).toArray();
			}
			return new Object[0];
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	protected Shell shell;
	private Text txt_target;
	private Table table;
	private TableViewer tableViewer;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			AdminToolsMain window = new AdminToolsMain();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(710, 433);
		shell.setText("管理小工具");
		shell.setLayout(new GridLayout(1, false));

		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));

		CTabFolder tabFolder = new CTabFolder(composite, SWT.BORDER);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

		CTabItem tabItem = new CTabItem(tabFolder, SWT.NONE);
		tabItem.setText("用户信息");

		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite_1);
		composite_1.setLayout(new GridLayout(1, false));

		Group group = new Group(composite_1, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));
		group.setText("搜索区");
		group.setBounds(0, 0, 735, 97);
		GridLayout gl_group = new GridLayout(2, false);
		gl_group.verticalSpacing = 10;
		gl_group.marginWidth = 10;
		gl_group.marginTop = 10;
		gl_group.marginRight = 10;
		gl_group.marginLeft = 10;
		gl_group.marginHeight = 10;
		gl_group.marginBottom = 10;
		group.setLayout(gl_group);

		Label label = new Label(group, SWT.NONE);
		label.setText("关键字");

		Composite composite_3 = new Composite(group, SWT.NONE);
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		composite_3.setLayout(new GridLayout(2, false));

		txt_target = new Text(composite_3, SWT.BORDER);
		txt_target.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Button button = new Button(composite_3, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Search Clicked");
				doGetUsers();
			}
		});
		button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));
		button.setText("搜索");

		Group group_1 = new Group(composite_1, SWT.NONE);
		group_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		group_1.setText("查询结果");
		group_1.setBounds(0, 0, 735, 211);
		group_1.setLayout(new GridLayout(1, false));

		tableViewer = new TableViewer(group_1, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.MULTI);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		TableViewerColumn tableViewerColumn = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn = tableViewerColumn.getColumn();
		tableColumn.setWidth(100);
		tableColumn.setText("用户ID");

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn_1 = tableViewerColumn_1.getColumn();
		tableColumn_1.setWidth(100);
		tableColumn_1.setText("登录账号");

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn_2 = tableViewerColumn_2.getColumn();
		tableColumn_2.setWidth(100);
		tableColumn_2.setText("昵称");

		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn_3 = tableViewerColumn_3.getColumn();
		tableColumn_3.setWidth(100);
		tableColumn_3.setText("登录失败次数");

		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn_4 = tableViewerColumn_4.getColumn();
		tableColumn_4.setWidth(100);
		tableColumn_4.setText("关联设备");

		TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn_5 = tableViewerColumn_5.getColumn();
		tableColumn_5.setWidth(100);
		tableColumn_5.setText("邮箱激活");

		Menu menu = new Menu(table);
		table.setMenu(menu);

		MenuItem menuItem = new MenuItem(menu, SWT.CASCADE);
		menuItem.setText("重置操作");

		Menu menu_1 = new Menu(menuItem);
		menuItem.setMenu(menu_1);

		MenuItem menuItem_1 = new MenuItem(menu_1, SWT.NONE);
		menuItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] selectItems = table.getSelection();
				for (TableItem selectItem : selectItems) {
					Object data = selectItem.getData();

					if (data instanceof UserVO) {
						IUserService userService = new UserServiceImpl();
						String id = ((UserVO) data).getId();
						System.out.println("resetFaildLoginTimes user, id= "
								+ id);
						userService.resetFaildLoginTimes(id);
						userService.destroy();
					}
				}
				doGetUsers();
			}
		});
		menuItem_1.setText("清除登录失败记录");

		MenuItem menuItem_2 = new MenuItem(menu_1, SWT.NONE);
		menuItem_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] selectItems = table.getSelection();
				for (TableItem selectItem : selectItems) {
					Object data = selectItem.getData();

					if (data instanceof UserVO) {
						IUserService userService = new UserServiceImpl();
						String id = ((UserVO) data).getId();
						System.out.println("cleanUserDeviceRels user, id= "
								+ id);
						userService.cleanUserDeviceRels(id);
						userService.destroy();
					}
				}
				doGetUsers();
			}
		});
		menuItem_2.setText("删除用户绑定关系");

		MenuItem menuItem_3 = new MenuItem(menu_1, SWT.NONE);
		menuItem_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] selectItems = table.getSelection();
				for (TableItem selectItem : selectItems) {
					Object data = selectItem.getData();

					if (data instanceof UserVO) {
						IUserService userService = new UserServiceImpl();
						String id = ((UserVO) data).getId();
						System.out.println("cleanMailRegister user, id= " + id);
						userService.cleanMailRegister(id);
						userService.destroy();
					}
				}
				doGetUsers();
			}
		});
		menuItem_3.setText("邮件去激活");

		MenuItem menuItem_4 = new MenuItem(menu, SWT.SEPARATOR);
		menuItem_4.setText("---分割线---");

		MenuItem menuItem_5 = new MenuItem(menu, SWT.NONE);
		menuItem_5.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] selectItems = table.getSelection();
				for (TableItem selectItem : selectItems) {
					Object data = selectItem.getData();

					if (data instanceof UserVO) {
						IUserService userService = new UserServiceImpl();
						String id = ((UserVO) data).getId();
						System.out.println("delUser user, id= " + id);
						userService.delUser(id);
						userService.destroy();
					}
				}
				doGetUsers();
			}
		});
		menuItem_5.setText("删除用户");
		tableViewer.setLabelProvider(new TableLabelProvider());
		tableViewer.setContentProvider(new ContentProvider());

		CTabItem tabItem_1 = new CTabItem(tabFolder, SWT.NONE);
		tabItem_1.setText("设备信息");

		Composite composite_2 = new DeviceInfoComposite(tabFolder, SWT.NONE);
		tabItem_1.setControl(composite_2);
		composite_2.setLayout(new GridLayout(1, false));

	}

	protected void doGetUsers() {

		try {
			IUserService userService = new UserServiceImpl();
			String target = txt_target.getText();
			List<UserVO> users = userService.getUsers(target);
			tableViewer.setInput(users);
			userService.destroy();

			System.out.println("Get User mail=" + target + ", result=" + users);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

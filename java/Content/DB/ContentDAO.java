package Content.DB;

import javax.naming.*;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContentDAO {
    private int result = 0;
    private DataSource ds;
    private final int PAGE_LIMIT = 10;
    private final int FIRST_START_PAGE = 1;
	private final int POPULAR_CONTENT_NUM = 7;


	public ContentDAO() {
		try {
			Context init = new InitialContext();
			ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public ContentBean contentSelect(int boardNum) {
		String query = "select * from chboard where boardnum = ?";
		ContentBean co = new ContentBean();
		try (Connection con = ds.getConnection(); PreparedStatement pst = con.prepareStatement(query);) {

			pst.setInt(1, boardNum);
			try (ResultSet rs = pst.executeQuery();) {

				if (rs.next()) {
					co.setBoardNum(rs.getInt(1));
					co.setChNum(rs.getInt(2));
					co.setWriter(rs.getString(3));
					co.setBoardTitle(rs.getString(4));
					co.setBoardContent(rs.getString(5));
					co.setBoardHeart(rs.getInt(6));
					co.setChcate_id(rs.getInt(7));
					co.setThumbNail(rs.getString(13));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return co;

	}

	public List<ContentBean> popcontentSelect() {
		String query = "select * from (select * from chboard order by (boardheart + boardvisit) desc) where rownum <= 7";
		List<ContentBean> contentList = new ArrayList<>();
		try (Connection conn = ds.getConnection();
				PreparedStatement pst = conn.prepareStatement(query);
				ResultSet rs = pst.executeQuery();) {

			while (rs.next()) {
				ContentBean co = new ContentBean();
				co.setBoardNum(rs.getInt(1));
				co.setChNum(rs.getInt(2));
				co.setWriter(rs.getString(3));
				co.setBoardTitle(rs.getString(4));
				co.setBoardContent(rs.getString(5));
				co.setThumbNail(rs.getString(13));
				contentList.add(co);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return contentList;
	}

	
    public List<ContentBean> contentSelectBycategory(int pageCount) {
        String query = "select * from (select * from (select rownum r, CHBOARD.* from chboard order by boardnum desc) where r between ? and ?)";
        int startRow = (pageCount - FIRST_START_PAGE) * PAGE_LIMIT + FIRST_START_PAGE;
        int endRow = pageCount * PAGE_LIMIT;
        List<ContentBean> contentSelectBycategory = new ArrayList<>();
        try (Connection conn = ds.getConnection();
             PreparedStatement pst = conn.prepareStatement(query);) {

            pst.setInt(1, startRow);
            pst.setInt(2, endRow);
            try (ResultSet rs = pst.executeQuery();) {
                while (rs.next()) {
                    ContentBean co = new ContentBean();
                    co.setBoardNum(rs.getInt("boardnum"));
                    co.setChNum(rs.getInt("ChNum"));
                    co.setWriter(rs.getString("Writer"));
                    co.setBoardTitle(rs.getString("BoardTitle"));
                    co.setBoardContent(rs.getString("BoardContent"));
                    co.setBoardHeart(rs.getInt("boardHeart"));
                    co.setChCate_id(rs.getInt("chCate_id"));
                    co.setBoardOpen(rs.getString("boardOpen"));
                    co.setBoardNore(rs.getString("boardNore"));
                    co.setBoardDate(rs.getTimestamp("boardDate"));
                    co.setBoardUpdate(rs.getTimestamp("boardUpdate"));
                    co.setThumbNail(rs.getString("ThumbNail"));
                    contentSelectBycategory.add(co);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return contentSelectBycategory;
    }

    public List<ContentBean> contentSelectBycategory(int channelCategoryId, int pageCount) {
        String query = "select * from (select * from (select rownum r, CHBOARD.* from chboard where chcate_id = ? order by boardnum desc) where r between ? and ?)";
        int startRow = (pageCount - FIRST_START_PAGE) * PAGE_LIMIT + FIRST_START_PAGE;
        int endRow = pageCount * PAGE_LIMIT;
        List<ContentBean> contentSelectBycategory = new ArrayList<>();

        try (Connection conn = ds.getConnection();
             PreparedStatement pst = conn.prepareStatement(query);) {

            pst.setInt(1, channelCategoryId);
            pst.setInt(2, startRow);
            pst.setInt(3, endRow);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    ContentBean co = new ContentBean();
                    co.setBoardNum(rs.getInt("boardnum"));
                    co.setChNum(rs.getInt("ChNum"));
                    co.setWriter(rs.getString("Writer"));
                    co.setBoardTitle(rs.getString("BoardTitle"));
                    co.setBoardContent(rs.getString("BoardContent"));
                    co.setBoardHeart(rs.getInt("boardHeart"));
                    co.setChCate_id(rs.getInt("chCate_id"));
                    co.setBoardOpen(rs.getString("boardOpen"));
                    co.setBoardNore(rs.getString("boardNore"));
                    co.setBoardDate(rs.getTimestamp("boardDate"));
                    co.setBoardUpdate(rs.getTimestamp("boardUpdate"));
                    co.setThumbNail(rs.getString("ThumbNail"));
                    contentSelectBycategory.add(co);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return contentSelectBycategory;
    }
    
	public int contentInsert(ContentBean co) {
		String query = "insert into chboard values(bo_seq.nextval,?,?,?,?,0,?,'Y','Y',sysdate,'',0)";

		try (Connection con = ds.getConnection(); PreparedStatement pst = con.prepareStatement(query)) {

			pst.setInt(1, co.getChNum());
			pst.setString(2, co.getWriter());
			pst.setString(3, co.getBoardTitle());
			pst.setString(4, co.getBoardContent());
			pst.setInt(5, co.getChcate_id());

			result = pst.executeUpdate();
			if (result == 1) {
				System.out.println("콘텐트 등록 완료");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public int getListCount() {
		String sql = "select count(*) from chboard";
		int x = 0;
		try (Connection con = ds.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					x = rs.getInt(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("getListCount() 에러: " + ex);
		}

		return x;
	} // getListCount() end

	public List<ContentBean> getContentList(int page, int limit) {
		// page : 페이지
		// limit : 페이지 당 목록의 수
		// board_re_ref desc, board_re_seq asc에 의해 정렬한 것을
		// 조건절에 맞는 rnum의 범위 만큼 가져오는 쿼리문입니다.

		String sql = "select * " + "from chboard ";
//				   + "where chcate_id = ? ";

//									"select *"
//								+ "from (select rownum rnum "
//								+ "	  from ( select chboard.*, nvl(cnt,0) as cnt "
//								+ "	  		 from chboard left outer join (select replyNum, count(*) cnt "
//								+ "	  									from boardreply "
//								+ "	  									group by replyNum) "
//								+ "	  		on boardNum = replyNum) "
////								+ "	  		order by BOARD_RE_REF desc, "
////								+ "	  		BOARD_RE_SEQ asc) j "
//								+ "	  where rownum <= ? "
// 
//								+ "	  ) "
//								+ " where rnum >= ? and rnum <= ?";

		List<ContentBean> list = new ArrayList<ContentBean>();
		// 한 페이지당 10개씩 목록인 경우 1페이지, 2페이지, 3페이지, 4페이지 ...
		int startrow = (page - 1) * limit + 1; // 읽기 시작할 row 번호(1 11 21 31 ...
		int endrow = startrow + limit - 1; // 읽을 마지막 row 번호(10 20 30 40 ...
		try (Connection con = ds.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql);) {
//					pstmt.setInt(1, endrow);
//					pstmt.setInt(2, startrow);
//					pstmt.setInt(3, endrow);
//					
			try (ResultSet rs = pstmt.executeQuery()) {

				// DB에서 가져온 데이터를 BoardBean에 담습니다.
				while (rs.next()) {
					ContentBean board = new ContentBean();
					board.setBoardNum(rs.getInt("BOARDNUM"));
					board.setChNum(rs.getInt("CHNUM"));
					board.setWriter(rs.getString("WRITER"));
					board.setBoardTitle(rs.getString("BOARDTITLE"));
					board.setBoardContent(rs.getString("BOARDCONTENT"));
					board.setBoardHeart(rs.getInt("BOARDHEART"));
					board.setBoardOpen(rs.getString("BOARDOPEN"));
					board.setBoardNore(rs.getString("BOARDNORE"));
					board.setBoardDate(rs.getTimestamp("BOARDDATE"));
					list.add(board); // 값을 담은 객체를 리스트에 저장합니다.
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("getContentList() 에러: " + ex);
		}
		return list;
		// getContentList() end

	}

	public List<ContentBean> channelhomeSelect() {
		String query = "select * from (select * from chboard order by boardvisit desc) where rownum <= 3";
		List<ContentBean> contentList = new ArrayList<>();

		try (Connection conn = ds.getConnection();
				PreparedStatement pst = conn.prepareStatement(query);
				ResultSet rs = pst.executeQuery();) {

			while (rs.next()) {
				ContentBean co = new ContentBean();
				co.setBoardNum(rs.getInt(1));
				co.setChNum(rs.getInt(2));
				co.setWriter(rs.getString(3));
				co.setBoardTitle(rs.getString(4));
				co.setBoardContent(rs.getString(5));
				co.setThumbNail(rs.getString(13));
				contentList.add(co);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return contentList;
	}

	// 게시물 목록을 가져오는 메소드입니다.
	public List<ContentBean> getBoardListByBoardNum(int chnum) {
		List<ContentBean> contentList = new ArrayList<>();

		String sql = "select * "
				+ "from (SELECT * from chboard "
				+ "	     where chnum = ?"
				+ "	     order by boardnum desc)"
				+ "		 where rownum <= 3";
		
		try (Connection conn = ds.getConnection(); 
				PreparedStatement pstmt = conn.prepareStatement(sql);) {

			pstmt.setInt(1, chnum);
			try (ResultSet rs = pstmt.executeQuery()) {

				while (rs.next()) {
					ContentBean co = new ContentBean();
					co.setBoardNum(rs.getInt(1));
					co.setChNum(rs.getInt(2));
					co.setWriter(rs.getString(3));
					co.setBoardTitle(rs.getString(4));
					co.setBoardContent(rs.getString(5));
					co.setThumbNail(rs.getString(13));
					contentList.add(co);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return contentList;
	}

}
